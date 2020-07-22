package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncTaskRunner extends AsyncTask<String, String, String> {

    private String resp;
    public ProgressDialog progressDialog;
    Context context;
    public AsyncTaskRunner(Context context)
    {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        publishProgress("In progress..."); // Calls onProgressUpdate()
        try {
            int time = Integer.parseInt(params[0])*1000;
            Thread.sleep(time);
            resp = "Slept for " + params[0] + " seconds";
        } catch (InterruptedException e) {
            e.printStackTrace();
            resp = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
        }
        return resp;
    }


    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
    }


    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context,
                "ProgressDialog",
                "Wait for few seconds");
    }


    @Override
    protected void onProgressUpdate(String... text) {
    }
}

