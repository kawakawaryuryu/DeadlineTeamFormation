package mail;

import net.arnx.jsonic.JSON;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Properties;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Slack {

	private String urlAsString;
	private String channel;
	private String username;
	private String icon_emoji;

	private void readProperties() {
		Properties prop = new Properties();
		String file = "properties/slack.properties";
		try {
			prop.load(new FileInputStream(file));

			urlAsString = prop.getProperty("url");
			channel = prop.getProperty("channel");
			username = prop.getProperty("username");
			icon_emoji = prop.getProperty("icon_emoji");
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

	private String getSuccessfulText(String subject, String msg) {
		StringBuilder text = new StringBuilder();
		// title
		text.append("*");
		text.append(subject);
		text.append("*");
		text.append("\n");

		// content
		text.append(msg);

		return text.toString();
	}

	private String getFailedText(String subject, String msg) {
		StringBuilder text = new StringBuilder();
		// title
		text.append("*");
		text.append(subject);
		text.append("*");
		text.append("\n");

		// content
		text.append("```\n");
		text.append(msg);
		text.append("\n```");

		return text.toString();
	}

    public void notifyToSlack(String subject, String msg, boolean success) {

		// POSTパラメータをファイルから取得
    	readProperties();

    	// Slackに通知するtextに変換
    	String text = success ? getSuccessfulText(subject, msg) : getFailedText(subject, msg);

        try {
            URL url = new URL(urlAsString);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");

            HashMap<String, String> data = new HashMap<String, String>(){
            	private static final long serialVersionUID = 1L;
				{
            		put("channel", channel);
            		put("username", username);
            		put("text", text);
            		put("icon_emoji", icon_emoji);
            	}
            };

            String field = "payload=" + URLEncoder.encode(JSON.encode(data), "utf-8");

            // POST送信
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            writer.write(field);
            writer.close();

            // POSTレスポンス
            BufferedReader reader =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            if (reader.lines().findFirst().get() == "OK") {
            	throw new Exception("Response is not correct from Slack");
            }

            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
