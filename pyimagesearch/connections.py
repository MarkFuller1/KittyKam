import time

from pyimagesearch.videomagager import stopStream

streamers = {}


def getNow():
    return round(time.time() * 1000)


def purgeStreamers():
    while True:
        time.sleep(1.0)
        # print("starting purge:" + str(streamers))

        toRemove = []

        for streamer in streamers:
            if getNow() - streamers[streamer] > 1000:
                toRemove.append(streamer)

        for disconnected in toRemove:
            print("purging: " + str(disconnected))
            del streamers[disconnected]

        if len(streamers) < 1:
            stopStream()
