package co.il.jabrutouch.ui.main.message_screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import co.il.jabrutouch.R;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.MessageObject;
import co.il.model.model.User;
import co.il.s3.interfaces.DownloadListener;
import co.il.s3.utils.Config;
import co.il.s3.utils.S3Helper;
import de.hdodenhof.circleimageview.CircleImageView;

import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.AUDIO;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.AUDIO_RECORD;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.DATE;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.LINK_MESSAGE;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.UNREAD_MESSAGES;
import static co.il.jabrutouch.ui.main.profile_screen.EditProfileActivity.USER_IMAGE;
import static co.il.s3.utils.S3Helper.PAGES_FILE;
import static org.litepal.LitePalApplication.getContext;




public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    private static final int IS_MINE_AND_IS_AUDIO = 324;
    private static final int IS_TEAM_AND_IS_AUDIO = 5734;
    private static final int IS_MINE = 423;
    private static final int IS_TEAM = 126;
    private static final int MESSAGE_DATE = 653;
    private static final int MESSAGE_UNREAD_MESSAGES = 475;
    public static final int LINK_CROWNS = 1;
    public static final int LINK_DOWNLOAD = 2;
    public static final int LINK_GEMARA = 3;
    public static final int LINK_MISHNA = 4;
    private final Context mContext;
    private final List<MessageObject> mMessageList;
    private final MessageAdapterListener mListener;
    private Runnable runnable;
    private Handler handler = new Handler();
    private int linkToTypeInt;
    private String linkToType;


    public MessageAdapter(Context mContext, List<MessageObject> mMessageList, MessageAdapterListener mListener) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
        this.mListener = mListener;
    }





    @Override
    public int getItemViewType(int position) {

        if (mMessageList.get(position).getIsMine()) {
            if (mMessageList.get(position).getMessageType() == AUDIO) {

                return IS_MINE_AND_IS_AUDIO;
            } else if (mMessageList.get(position).getMessageType() == DATE) {

                return MESSAGE_DATE;
            } else if (mMessageList.get(position).getMessageType() == UNREAD_MESSAGES) {

                return MESSAGE_UNREAD_MESSAGES;


            } else {

                return IS_MINE;
            }
        } else {

            if (mMessageList.get(position).getMessageType() == AUDIO) {
                return IS_TEAM_AND_IS_AUDIO;
            }

            return IS_TEAM;
        }
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == IS_MINE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meesage_user_item, parent, false);
            return new ViewHolder(view);
        } else if (viewType == IS_MINE_AND_IS_AUDIO) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meesage_user_audio_item, parent, false);
            return new AudioViewHolder(view);

        } else if (viewType == MESSAGE_DATE) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meesage_date, parent, false);
            return new DateViewHolder(view);

        } else if (viewType == MESSAGE_UNREAD_MESSAGES) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meesage_unread, parent, false);
            return new DateViewHolder(view);

        } else if (viewType == IS_TEAM_AND_IS_AUDIO) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meesage_team_audio_item, parent, false);
            return new AudioViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meesage_team_item, parent, false);
            return new ViewHolder(view);
        }

    }





    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        setProfilePicture(holder, position);

        if (mMessageList.get(position).getIsMine() &&
                mMessageList.get(position).getMessageType() == AUDIO) {

            ((AudioViewHolder) holder).updateAudioItem(position);

            if (existInLocalStorage(mMessageList.get(position).getMessage())) {

                mMessageList.get(position).setMessage(Config.getPathName(mContext) + AUDIO_RECORD + "/" + getFIleNameFromFileKEy(mMessageList.get(position).getMessage()));
                setAudioPlayer((AudioViewHolder) holder, mMessageList.get(position));


            } else {


                downloadRecordAudio(mMessageList.get(position), holder);
            }


        } else if (mMessageList.get(position).getIsMine() && mMessageList.get(position).getMessageType() == DATE) {

            ((DateViewHolder) holder).updateDateItem(position);


        } else if (mMessageList.get(position).getIsMine() && mMessageList.get(position).getMessageType() == UNREAD_MESSAGES) {

            ((DateViewHolder) holder).updateDateItem(position);


        } else {
            if (mMessageList.get(position).getMessageType() == AUDIO) {

                ((AudioViewHolder) holder).updateAudioItem(position);

                if (mMessageList.get(position).getIsMine()) {

                    setAudioPlayer((AudioViewHolder) holder, mMessageList.get(position));
                    ((AudioViewHolder) holder).mProgressBarPB.setVisibility(View.GONE);

                } else {

                    if (existInLocalStorage(mMessageList.get(position).getMessage())) {

                        ((AudioViewHolder) holder).mProgressBarPB.setVisibility(View.GONE);
                        mMessageList.get(position).setMessage(Config.getPathName(mContext) + AUDIO_RECORD + "/" + getFIleNameFromFileKEy(mMessageList.get(position).getMessage()));
                        setAudioPlayer((AudioViewHolder) holder, mMessageList.get(position));

                    } else {

                        ((AudioViewHolder) holder).mProgressBarPB.setVisibility(View.VISIBLE);
                        ((AudioViewHolder) holder).mAudioTimeTV.setText("");
                        downloadRecordAudio(mMessageList.get(position), holder);
                    }
                }


            } else {

                holder.updateItem(position);
            }

        }
    }

    private void setProfilePicture(ViewHolder holder, int position) {

        switch (getItemViewType(position)){

            case IS_MINE:
            case IS_MINE_AND_IS_AUDIO:

                setUserImage(holder.mUserImage, holder.mImageProgress);
                break;

            case IS_TEAM:
            case IS_TEAM_AND_IS_AUDIO:

                setTeamImage(holder.mRabbyImage, mMessageList.get(position));
                break;

        }




    }





    private void setUserImage(final ImageView mUserImage, final ProgressBar mImageProgress) {

        Gson gson = new Gson();
        final User userFromJson = gson.fromJson(UserManager.getUser(getContext()), User.class);

        if (userFromJson.getImage() != null && !userFromJson.getImage().equals("")) {

            if (imageExistInLocalStorage(userFromJson.getImage())) {

                String localImageLink = Config.getPathName(getContext()) + USER_IMAGE + "/" + getFIleNameFromFileKEy(userFromJson.getImage());
                Bitmap myBitmap = BitmapFactory.decodeFile(localImageLink);
                mUserImage.setImageBitmap(myBitmap);

            } else {

                mImageProgress.setVisibility(View.VISIBLE);
                S3Helper s3Helper = new S3Helper(getContext());

                s3Helper.downloadFile(getContext(), userFromJson.getImage().substring(1), userFromJson.getImage().substring(1), USER_IMAGE + "/", new DownloadListener() {
                    @Override
                    public void onDownloadStart(int id, File fileLocation) {

                    }

                    @Override
                    public void onProgressChanged(int percentDone) {

                    }

                    @Override
                    public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

                    }

                    @Override
                    public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

                    }

                    @Override
                    public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {

                        mUserImage.setImageBitmap(getImageBitmap(pathName));
                        mImageProgress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onDownloadError() {

                    }
                });
            }

        }
    }




    /**
     * parse image from url to bitmap
     *
     * @param url String
     * @return Bitmap
     */
    private Bitmap getImageBitmap(String url) {
        Bitmap bitmap = BitmapFactory.decodeFile(url);

//                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true); to resize the image

        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(url);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;

    }






    private void setTeamImage(ImageView mRabbyImage, MessageObject messageObject) {
    }







    private boolean existInLocalStorage(String message) {

        File file = new File(Config.getPathName(mContext) + AUDIO_RECORD + "/" + getFIleNameFromFileKEy(message));

        return file.exists();

    }



    private boolean imageExistInLocalStorage(String userIamge) {

        File file = new File(Config.getPathName(getContext()) + USER_IMAGE + "/" + getFIleNameFromFileKEy(userIamge));

        return file.exists();

    }






    private String getFIleNameFromFileKEy(String fileKey) {

        return fileKey.substring(fileKey.lastIndexOf("/") + 1);


    }




    private void downloadRecordAudio(MessageObject messageObject, ViewHolder holder) {

        mListener.downloadRecordAudio(messageObject, holder);

    }





    @SuppressLint("ResourceAsColor")
    private void setAudioPlayer(final AudioViewHolder holder, final MessageObject message) {

        Uri uri = Uri.parse(message.getMessage());


        final MediaPlayer mMediaPlayer;

        mMediaPlayer = MediaPlayer.create(mContext, uri);

        int duration = mMediaPlayer.getDuration();
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                (TimeUnit.MILLISECONDS.toSeconds(duration) + 1) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );

        holder.mAudioTimeTV.setText(time);

        final SeekBar seekBar = (SeekBar) holder.mAudioSeekBar;
        seekBar.setProgress(0);


        holder.mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();

                    if (message.getIsMine()){

                        holder.mPlayBtn.setImageResource(R.drawable.ic_play_white);
                    }else {

                        holder.mPlayBtn.setImageResource(R.drawable.ic_play22);

                    }


                    int duration = mMediaPlayer.getDuration();
                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                    );

                    holder.mAudioTimeTV.setText(time);

                } else {
                    mMediaPlayer.start();

                    if (message.getIsMine()){

                        holder.mPlayBtn.setImageResource(R.drawable.ic_pause_white);

                    }else {

                        holder.mPlayBtn.setImageResource(R.drawable.ic_jabru_pause_new);
                    }

                    int duration = mMediaPlayer.getCurrentPosition();
                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                    );

                    holder.mAudioTimeTV.setText(time);


                    changeSeekBar(seekBar, mMediaPlayer, holder, message);

                }
            }
        });




        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mp.getDuration());
                changeSeekBar(seekBar, mp, holder, message);
            }

            ;
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }





    private void changeSeekBar(final SeekBar seekBar, final MediaPlayer mediaPlayer, final AudioViewHolder holder, final MessageObject message) {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {

                    changeSeekBar(seekBar, mediaPlayer, holder, message);

                    if (mediaPlayer.getCurrentPosition() + 200 >= mediaPlayer.getDuration()) {

                        if (message.getIsMine()){

                            holder.mPlayBtn.setImageResource(R.drawable.ic_play_white);
                        }else {

                            holder.mPlayBtn.setImageResource(R.drawable.ic_play22);

                        }


                        int duration = mediaPlayer.getCurrentPosition();
                        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                                TimeUnit.MILLISECONDS.toMinutes(duration),
                                (TimeUnit.MILLISECONDS.toSeconds(duration) + 1) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                        );

                        holder.mAudioTimeTV.setText(time);
                    }

                    if (mediaPlayer.isPlaying()) {

                        int duration = mediaPlayer.getCurrentPosition();
                        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                                TimeUnit.MILLISECONDS.toMinutes(duration),
                                (TimeUnit.MILLISECONDS.toSeconds(duration) + 1) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                        );

                        holder.mAudioTimeTV.setText(time);
                    }
                }
            };

            handler.postDelayed(runnable, 1000);
        }
    }





    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void setAudioPlayerAfterDownloaded(MessageObject messageObject, ViewHolder holder) {

        ((AudioViewHolder) holder).mProgressBarPB.setVisibility(View.GONE);

        setAudioPlayer((AudioViewHolder) holder, messageObject);

    }




    public void onAudioDownloadError(ViewHolder holder) {

        Toast.makeText(mContext, mContext.getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
        ((AudioViewHolder) holder).mProgressBarPB.setVisibility(View.GONE);

    }





    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mMessage;
        private final TextView mMessageTime;
        private final ImageView mRabbyImage;
        private final CircleImageView mUserImage;
        private final ProgressBar mImageProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.MUI_message_TV);
            mMessageTime = itemView.findViewById(R.id.MUI_message_time_TV);
            mRabbyImage = itemView.findViewById(R.id.MTI_team_imageIV);
            mUserImage = itemView.findViewById(R.id.MUI_user_image_IV);
            mImageProgress = itemView.findViewById(R.id.MI_image_progress_bar);

        }

        public void updateItem(int position) {

            if (mMessageList.get(position).getMessage() != null) {

                mMessage.setText(mMessageList.get(position).getMessage());

                String messageDAte = DateFormat.format("HH:mm", mMessageList.get(position).getSentAt()).toString();

                mMessageTime.setText(messageDAte);

//                if (!mMessageList.get(position).getIsMine() && mMessageList.get(position).getImage() != null &&
//                        !mMessageList.get(position).getImage().equals("")) {
//
//                    setImageFromS3(mMessageList.get(position), mRabbyImage);
//
//                }
//
//
//                if (mMessageList.get(position).getIsMine() && mMessageList.get(position).getImage() != null &&
//                        !mMessageList.get(position).getImage().equals("")) {
//
//                    setImageFromS3(mMessageList.get(position), mUserImage);
//
//                }


                String messageTypeString = String.valueOf(mMessageList.get(position).getMessageType());

                if (Integer.valueOf(messageTypeString.substring(0, 1)) == LINK_MESSAGE){

                    linkToTypeInt = Integer.valueOf(messageTypeString.substring(1));


                    mMessage.setPaintFlags(mMessage.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                    mMessage.setTextColor(getContext().getResources().getColor(R.color.bottom_nevi_blue));

                    mMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mListener.onLinkMessageClicked(linkToTypeInt);
                        }
                    });

                }

            }


        }





        private void setImageFromS3(MessageObject messageObject, final ImageView mRabbyImage) {


            S3Helper s3Helper = new S3Helper(getContext());
            s3Helper.downloadFile(getContext(), messageObject.getImage(), messageObject.getImage(), PAGES_FILE, new DownloadListener() {
                @Override
                public void onDownloadStart(int id, File fileLocation) {
                }

                @Override
                public void onProgressChanged(int percentDone) {

                }

                @Override
                public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

                }

                @Override
                public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

                }

                @Override
                public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {


                    File imgFile = new File(pathName);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        mRabbyImage.setImageBitmap(myBitmap);

                    }


                }

                @Override
                public void onDownloadError() {

                }
            });


        }
    }




    private class AudioViewHolder extends ViewHolder {

        private final ImageView mPlayBtn;
        private final SeekBar mAudioSeekBar;
        private final TextView mAudioTimeTV;
        private final TextView mTimeTV;
        private final ProgressBar mProgressBarPB;

        public AudioViewHolder(View view) {
            super(view);
            mPlayBtn = view.findViewById(R.id.MUIA_play_btn);
            mAudioSeekBar = view.findViewById(R.id.MUIA_seekbar_SB);
            mAudioTimeTV = view.findViewById(R.id.MUAI_audio_time_TV);
            mTimeTV = view.findViewById(R.id.MUAI_time);
            mProgressBarPB = view.findViewById(R.id.MUAI_progress_bar_PB);
        }

        public void updateAudioItem(int position) {

            String messageDAte = DateFormat.format("HH:mm", mMessageList.get(position).getSentAt()).toString();

            mTimeTV.setText(messageDAte);

        }
    }



    public class DateViewHolder extends ViewHolder {


        private final TextView mDate;


        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.MUI_message_TV);

        }

        public void updateDateItem(int position) {

            mDate.setText(mMessageList.get(position).getMessage());
        }


    }


    public interface MessageAdapterListener {

        void downloadRecordAudio(MessageObject messageObject, ViewHolder holder);

        void onLinkMessageClicked(int linkToType);
    }

}
