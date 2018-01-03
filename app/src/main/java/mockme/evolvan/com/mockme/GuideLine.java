package mockme.evolvan.com.mockme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MyPc on 16-Oct-17.
 */

public class GuideLine extends AppCompatActivity {

    TextView close,developer;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_line);
        close = (TextView) findViewById(R.id.close);
        developer=(TextView) findViewById(R.id.developer);
        /*if (developer != null) {
            developer.setMovementMethod(LinkMovementMethod.getInstance());
        }*/
        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/studio/debug/dev-options.html#enable"));
                startActivity(browser);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuideLine.this.finish();
            }
        });
    }
}