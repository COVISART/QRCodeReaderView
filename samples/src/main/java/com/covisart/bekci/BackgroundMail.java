package com.covisart.bekci;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.covisart.bekci.GmailSender;

/**
 * @author Kristijan Drača https://plus.google.com/u/0/+KristijanDrača
 */
public class BackgroundMail {
    String TAG = "Contact Form Library";
    String username, password, mailto, subject, body, sendingMessage,
            sendingMessageSuccess;
    Context mContext;

    public BackgroundMail(Context context) {
        this.mContext = context;
    }

    public void setGmailUserName(String string) {
        this.username = string;
    }

    public void setGmailPassword(String string) {
        this.password = string;
    }

    public void setMailTo(String string) {
        this.mailto = string;
    }

    public void setFormSubject(String string) {
        this.subject = string;
    }

    public void setFormBody(String string) {
        this.body = string;
    }

    public void setSendingMessage(String string) {
        this.sendingMessage = string;
    }

    public void setSendingMessageSuccess(String string) {
        this.sendingMessageSuccess = string;
    }

    public void send() {
        boolean valid = true;
        if (username == null && username.isEmpty()) {
            Log.e(TAG, "You didn't set Gmail username!");
            valid = false;
        }
        if (password == null && password.isEmpty()) {
            Log.e(TAG, "You didn't set Gmail password!");
            valid = false;
        }
        if (mailto == null && mailto.isEmpty()) {
            Log.e(TAG, "You didn't set email recipient!");
            valid = false;
        }
        if (Utils.isNetworkAvailable(mContext) == false) {
            Log.e(TAG, "User don't have internet connection!");
            valid = false;
        }
        if (valid == true) {
            new startSendingEmail().execute();
        }
    }

    public class startSendingEmail extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(mContext);
            if (sendingMessage == null && sendingMessage.isEmpty()) {
                Log.d(TAG, "We dont have sending message so we use generic");
                pd.setMessage("Loading...");
            } else {
                pd.setMessage(sendingMessage);
            }
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                GmailSender sender = new GmailSender(username, password);
                sender.sendMail(subject, body, username, mailto);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            if (sendingMessageSuccess == null && sendingMessageSuccess.isEmpty()) {
                Log.d(TAG,
                        "We dont have sending success message so we use generic");
                Toast.makeText(mContext, "Your message was sent successfully.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, sendingMessageSuccess,
                        Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

}
