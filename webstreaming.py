# import the necessary packages
from flask import Response
from flask import Flask, request
from flask import render_template
import threading
import argparse
import datetime
import imutils
import cv2
from pyimagesearch.connections import streamers, getNow, purgeStreamers
import asyncio
from pyimagesearch.videomagager import stream, restartStream, streamIsOff, streamRead

# initialize the output frame and a lock used to ensure thread-safe
# exchanges of the output frames (useful when multiple browsers/tabs
# are viewing the stream)
from pyimagesearch.motion_detection.singlemotiondetector import SingleMotionDetector

outputFrame = None
lock = threading.Lock()
# initialize a flask object
app = Flask(__name__)


# initialize the video stream and allow the camera sensor to
# warmup


@app.route("/connections")
def connections():
    return len(streamers)


@app.route("/")
def index():
    # return the rendered template
    return render_template("index.html", value=len(streamers))


def detect_motion(frameCount):
    # print("detect motion")
    # grab global references to the video stream, output frame, and
    # lock variables

    global outputFrame, lock
    # initialize the motion detector and the total number of frames
    # read thus far
    md = SingleMotionDetector(accumWeight=0.1)
    total = 0

    # loop over frames from the video stream
    while True:
        # print("detectMotion loop")
        if streamIsOff():
            continue
        # read the next frame from the video stream, resize it,
        # convert the frame to grayscale, and blur it
        frame = streamRead()
        frame = imutils.resize(frame, width=720, height=520)
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        gray = cv2.GaussianBlur(gray, (7, 7), 0)
        # grab the current timestamp and draw it on the frame
        timestamp = datetime.datetime.now()
        cv2.putText(frame, timestamp.strftime(
            "%A %d %B %Y %I:%M:%S%p"), (10, frame.shape[0] - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.35, (0, 0, 255), 1)

        # if the total number of frames has reached a sufficient
        # number to construct a reasonable background model, then
        # continue to process the frame
        if total > frameCount:
            # detect motion in the image
            motion = md.detect(gray)
            # check to see if motion was found in the frame
            if motion is not None:
                # unpack the tuple and draw the box surrounding the
                # "motion area" on the output frame
                (thresh, (minX, minY, maxX, maxY)) = motion
                cv2.rectangle(frame, (minX, minY), (maxX, maxY),
                              (0, 0, 255), 2)

        # update the background model and increment the total number
        # of frames read thus far
        md.update(gray)
        total += 1
        # acquire the lock, set the output frame, and release the
        # lock
        with lock:
            outputFrame = frame.copy()


def generate(streamer):
    # print("generate")
    # grab global references to the output frame and lock variables
    global outputFrame, lock
    # loop over frames from the output stream

    streamers[streamer] = getNow()

    while True:
        # print("generate loop")
        restartStream()
        streamers[streamer] = getNow()
        # wait until the lock is acquired
        with lock:
            # check if the output frame is available, otherwise skip
            # the iteration of the loop
            if outputFrame is None:
                print("frame not ready")
                continue
            # encode the frame in JPEG format
            (flag, encodedImage) = cv2.imencode(".jpg", outputFrame)
            # ensure the frame was successfully encoded
            if not flag:
                continue
        # yield the output frame in the byte format
        yield (b'--frame\r\n' b'Content-Type: image/jpeg\r\n\r\n' +
               bytearray(encodedImage) + b'\r\n')


@app.route("/video_feed")
def video_feed():
    # return the response generated along with the specific media
    # type (mime type)
    response = Response(generate(str(request.remote_addr)),
                        mimetype="multipart/x-mixed-replace; boundary=frame")
    return response


# check to see if this is the main thread of execution
if __name__ == '__main__':
    # construct the argument parser and parse command line arguments
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--ip", type=str, required=True,
                    help="ip address of the device")
    ap.add_argument("-o", "--port", type=int, required=True,
                    help="ephemeral port number of the server (1024 to 65535)")
    ap.add_argument("-f", "--frame-count", type=int, default=32,
                    help="# of frames used to construct the background model")
    args = vars(ap.parse_args())
    # start a thread that will perform motion detection
    addText = threading.Thread(target=detect_motion, args=(
        args["frame_count"],))
    addText.daemon = True
    addText.start()

    # purge streamers list
    t = threading.Thread(target=purgeStreamers, args=())
    t.daemon = True
    t.start()

    # start the flask app
    asyncio.run(app.run(host=args["ip"], port=args["port"], debug=True,
                        threaded=True, use_reloader=False))

# release the video stream pointer
stream.stop()
