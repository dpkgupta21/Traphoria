package com.app.traphoria.crashreport;

/**
 * Created by DeepakGupta on 1/24/16.
 */

import android.content.Context;
import android.content.Intent;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;


public class ACRAReportSender implements ReportSender {
    private String emailUsername;
    private String emailPassword;

    public ACRAReportSender(String emailUsername, String emailPassword) {
        super();
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
    }


    @Override
    public void send(Context context, CrashReportData report)

            throws ReportSenderException

    {

        // Extract the required data out of the crash report.
        String reportBody = createCrashReport(report);

        Intent emailIntent = new Intent(
                Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Crash report");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                "dpk.gupta21@gmail.com" +
                        "");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                reportBody);
        context.startActivity(emailIntent);
        // instantiate the email sender
        /*GMailSender gMailSender = new GMailSender(emailUsername, emailPassword);
        try {
            // specify your recipients and send the email

            gMailSender.sendMail("CRASH REPORT",
                    reportBody,
                    "dpkgupta.thepsi@gmail.com",
                    "dpk.gupta21@gmail.com," +
                            " dpk.gpt890@gmail.com");
        } catch (Exception e) {
            Log.d("Error Sending email", e.toString());
        }*/
    }


    private String createCrashReport(CrashReportData report) {

        // I've extracted only basic information.
        // U can add loads more data using the enum ReportField. See below.
        StringBuilder body = new StringBuilder();
        body.append("Device : " + report.getProperty(ReportField.BRAND) +
                "-" + report.getProperty(ReportField.PHONE_MODEL))
                .append("\n")
                .append("Android Version :" + report.getProperty(ReportField.ANDROID_VERSION))
                .append("\n")
                .append("App Version : " + report.getProperty(ReportField.APP_VERSION_CODE))
                .append("\n")
                .append("STACK TRACE : \n" + report.getProperty(ReportField.STACK_TRACE));
        return body.toString();
    }
}

