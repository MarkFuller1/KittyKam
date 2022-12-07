import threading
import time

from imutils.video import VideoStream

stream = None
time.sleep(1)
lastStop = time.time()

lock = threading.Lock()


def restartStream():
    # print("restart stream start")
    # print("restartStream locked")
    global stream
    if streamIsOff():
        global lock
        with lock:
            print("starting stream")
            stream = VideoStream(src=0).start()
            time.sleep(1)


# print("restart stream stop")


def stopStream():
    # print("stopStream start")
    global lastStop
    # print("it is currently: " + str(time.time()) + " the last stop was:" + str(lastStop) + " " + str(
    #     time.time() - lastStop) + " seconds left")
    if time.time() - lastStop > 5 and streamIsOn():
        global lock
        with lock:
            # print("stopStream locked")
            global stream
            stream.stream.release()
            stream = None
            lastStop = time.time()

    # print("stopStream stop")


def streamIsOn():
    # print("streamison start")
    global lock
    with lock:
        # print("streamIsOn locked")
        global stream
        # print("stream is ..." + ("on" if stream is not None else "off"))
        # print("streamison stop")
        return stream is not None


def streamIsOff():
    # print("streamisoff start")
    global lock
    with lock:
        # print("streamisoff lock")
        global stream
        # print("stream is ..." + ("on" if stream is not None else "off"))
        # print("streamisoff stop")
        return stream is None


def streamRead():
    # print("streamread start")
    global lock
    with lock:
        # print("streamread locked")
        global stream
        # print("streamread stop")
        return stream.read()
