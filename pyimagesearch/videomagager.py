import time

from imutils.video import VideoStream

stream = None
time.sleep(1)
lastStop = time.time()


def restartStream():
    global stream
    if streamIsOff():
        print("starting stream")
        stream = VideoStream(src=0).start()
        time.sleep(1)


def stopStream():
    global lastStop
    # print("it is currently: " + str(time.time()) + " the last stop was:" + str(lastStop) + " " + str(
    #     time.time() - lastStop) + " seconds left")
    if time.time() - lastStop > 5 and streamIsOn():
        print("stopping stream")
        global stream
        stream.stream.release()
        stream = None



def streamIsOn():
    global stream
    return stream is not None


def streamIsOff():
    global stream
    return stream is None


def streamRead():
    global stream
    return stream.read()
