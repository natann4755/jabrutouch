package co.il.jabrutouch.ui.main.video_screen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import java.io.File;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.Gallery;
import co.il.s3.interfaces.DownloadListener;
import co.il.s3.utils.S3Helper;
import static co.il.s3.utils.S3Helper.PAGES_FILE;




public class GalleryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Gallery mImage;
    private ImageView mImageView;
    private ProgressBar mProgressBar;


    public GalleryFragment() {
    }


    public static GalleryFragment newInstance(Gallery mImage) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, mImage);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImage = (Gallery) getArguments().getSerializable(ARG_PARAM1);
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImageView = Objects.requireNonNull(getView()).findViewById(R.id.FG_pdfView_PDFV);
        mProgressBar = getView().findViewById(R.id.FG_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);


        S3Helper s3Helper = new S3Helper(getContext());
        s3Helper.downloadFile(getContext(), mImage.getImage(), mImage.getImage(), PAGES_FILE, new DownloadListener() {
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
  ;           File imgFile = new  File(pathName);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    mImageView.setImageBitmap(myBitmap);
                    mProgressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onDownloadError() {

            }
        });

    }




    @Override
    public void onDetach() {
        super.onDetach();
    }



}
