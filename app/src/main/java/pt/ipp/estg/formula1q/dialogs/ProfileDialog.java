package pt.ipp.estg.formula1q.dialogs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.activities.LoginActivity;

public class ProfileDialog extends DialogFragment {
    private FirebaseUser user;
    private Bitmap userPic;
    private TextView tv_username, tv_email;
    private ImageView iv_userPic;
    private Button btn_signOut;

    public ProfileDialog(Bitmap userPic) {
        super();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.userPic = userPic;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_profile, container);

        iv_userPic = view.findViewById(R.id.profile_dialog_user_icon);
        tv_username = view.findViewById(R.id.profile_dialog_username);
        tv_email = view.findViewById(R.id.profile_dialog_email);
        btn_signOut = view.findViewById(R.id.profile_dialog_signout);

        iv_userPic.setImageBitmap(userPic);
        tv_username.setText(user.getDisplayName());
        tv_email.setText(user.getEmail());
        btn_signOut.setOnClickListener(v -> {
            Intent signOutIntent = new Intent(getContext(), LoginActivity.class);
            signOutIntent.putExtra("shouldSignOut", true);
            startActivity(signOutIntent);
            getDialog().dismiss();
        });

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }
}
