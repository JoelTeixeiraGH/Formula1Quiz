package pt.ipp.estg.formula1q.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pt.ipp.estg.formula1q.database.question.QuestionRepository;

public class CameraActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView ScannerView;
    private int questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);
    }

    @Override
    public void handleResult(Result result) {
        try {
            questionId = Integer.valueOf(result.getText());
            QuestionRepository.getInstance(this).getQuestionById(question ->{
                Intent intent;
                if(question != null){
                    intent = new Intent(this, QuestionActivity.class);
                    intent.putExtra("sharedQuestionId", questionId);
                    intent.putExtra("isSharedQuestion", true);
                    intent.putExtra("isSpecialQuestion", true);

                }else{
                    Toast.makeText(this, "Invalid QRCode", Toast.LENGTH_SHORT).show();
                    intent = new Intent(this, MainActivity.class);
                }
                startActivity(intent);
            },questionId);
        }catch (Exception e){
            Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}