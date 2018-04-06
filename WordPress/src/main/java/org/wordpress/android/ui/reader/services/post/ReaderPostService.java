package org.wordpress.android.ui.reader.services.post;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import org.wordpress.android.models.ReaderTag;
import org.wordpress.android.ui.reader.ReaderEvents;
import org.wordpress.android.ui.reader.services.ServiceCompletionListener;
import org.wordpress.android.util.AppLog;

import de.greenrobot.event.EventBus;

/**
 * service which updates posts with specific tags or in specific blogs/feeds - relies on
 * EventBus to alert of update status
 */

public class ReaderPostService extends Service implements ServiceCompletionListener {
    private static final String ARG_TAG = "tag";
    private static final String ARG_ACTION = "action";
    private static final String ARG_BLOG_ID = "blog_id";
    private static final String ARG_FEED_ID = "feed_id";

    private ReaderPostLogic mReaderPostLogic;

    public enum UpdateAction {
        REQUEST_NEWER, // request the newest posts for this tag/blog/feed
        REQUEST_OLDER, // request posts older than the oldest existing one for this tag/blog/feed
        REQUEST_OLDER_THAN_GAP // request posts older than the one with the gap marker for this tag
                               // (not supported for blog/feed)
    }

    /*
     * update posts with the passed tag
     */
    public static void startServiceForTag(Context context, ReaderTag tag, UpdateAction action) {
        Intent intent = new Intent(context, ReaderPostService.class);
        intent.putExtra(ARG_TAG, tag);
        intent.putExtra(ARG_ACTION, action);
        context.startService(intent);
    }

    /*
     * update posts in the passed blog
     */
    public static void startServiceForBlog(Context context, long blogId, UpdateAction action) {
        Intent intent = new Intent(context, ReaderPostService.class);
        intent.putExtra(ARG_BLOG_ID, blogId);
        intent.putExtra(ARG_ACTION, action);
        context.startService(intent);
    }

    /*
     * update posts in the passed feed
     */
    public static void startServiceForFeed(Context context, long feedId, UpdateAction action) {
        Intent intent = new Intent(context, ReaderPostService.class);
        intent.putExtra(ARG_FEED_ID, feedId);
        intent.putExtra(ARG_ACTION, action);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReaderPostLogic = new ReaderPostLogic(this);
        AppLog.i(AppLog.T.READER, "reader post service > created");
    }

    @Override
    public void onDestroy() {
        AppLog.i(AppLog.T.READER, "reader post service > destroyed");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }

        UpdateAction action;
        if (intent.hasExtra(ARG_ACTION)) {
            action = (UpdateAction) intent.getSerializableExtra(ARG_ACTION);
        } else {
            action = UpdateAction.REQUEST_NEWER;
        }

        EventBus.getDefault().post(new ReaderEvents.UpdatePostsStarted(action));

        if (intent.hasExtra(ARG_TAG)) {
            ReaderTag tag = (ReaderTag) intent.getSerializableExtra(ARG_TAG);
            mReaderPostLogic.performTask(null, action, tag, -1, -1);
        } else if (intent.hasExtra(ARG_BLOG_ID)) {
            long blogId = intent.getLongExtra(ARG_BLOG_ID, 0);
            mReaderPostLogic.performTask(null, action, null, blogId, -1);
        } else if (intent.hasExtra(ARG_FEED_ID)) {
            long feedId = intent.getLongExtra(ARG_FEED_ID, 0);
            mReaderPostLogic.performTask(null, action, null, -1, feedId);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCompleted(Object companion) {
        stopSelf();
    }
}
