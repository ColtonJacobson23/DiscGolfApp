
import cv2 as cv
import random

# Source: https://livecodestream.dev/post/object-tracking-with-opencv/


def object_tracking_live_video(video):

    # Tracker

    #tracker = cv.TrackerKCF_create()
    tracker = cv.legacy.TrackerTLD_create()
    #tracker = cv.TrackerGOTURN_create()
    # tracker = cv.legacy.TrackerMOSSE_create()
    # tracker  = cv.TrackerCSRT_create()

    # Read the first frame
    ok, frame = video.read()
    if not ok:
        print("Could not read the first frame")
        return

    # Region of interest: Manually select a bounding box for the detected object
    box = cv.selectROI(frame, False)

    # Initialize the tracker
    _ = tracker.init(frame, box)

    while True:

        # Read the first frame
        ok, frame = video.read()
        if not ok:
            break

        # Update the tracker at each tick
        ok, box = tracker.update(frame)
        if ok:
            p1 = (int(box[0]), int(box[1]))
            p2 = (int(box[0]) + int(box[2]), int(box[1]) + int(box[3]))
            # Place the box on top of video
            cv.rectangle(frame, p1, p2, (0, 0, 255), 2, 1)

        cv.putText(frame, "Sport Campanion", (10, 30),
                   cv.FONT_HERSHEY_COMPLEX_SMALL, 0.75, (0, 0, 0), 2)
        cv.imshow("Object Tracking", frame)

        if cv.waitKey(1) & 0xFF == ord('q'):
            break

    video.release()
    cv.destroyAllWindows()


def main():
    video = cv.VideoCapture(0)
    object_tracking_live_video(video)


if __name__ == '__main__':
    main()
