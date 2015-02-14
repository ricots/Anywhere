package com.example.anywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class ChooseActivity extends Activity {

	private RadioButton rb1;
	private RadioButton rb2;
	private Button nextButton;
	private Button quitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);
		rb1 = (RadioButton) findViewById(R.id.radiobutton1);
		rb2 = (RadioButton) findViewById(R.id.radiobutton2);
		nextButton = (Button) findViewById(R.id.next_button);
		quitButton = (Button) findViewById(R.id.quit_button);
		nextButton.setOnClickListener(new NextListener());
		quitButton.setOnClickListener(new QuitListener());
	}

	private class NextListener implements OnClickListener {
		public void onClick(View v) {
			String inOut = "in";
			if (ChooseActivity.this.rb1.isChecked()) {
				inOut = "in";
			} else if (ChooseActivity.this.rb2.isChecked()) {
				inOut = "out";
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.none_choose),
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent();
			intent.putExtra("InOut", inOut);
			if (ChooseActivity.this.rb1.isChecked()) {
				intent.setClass(ChooseActivity.this, IndoorActivity.class);
				ChooseActivity.this.startActivity(intent);
			} else if (ChooseActivity.this.rb2.isChecked()) {
				intent.setClass(ChooseActivity.this, MainActivity.class);
				ChooseActivity.this.startActivity(intent);
			}
		}
	}

	private class QuitListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ChooseActivity.this.startActivity(intent);
			System.exit(0);
		}
	}
}
