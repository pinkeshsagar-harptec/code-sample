package com.example.rssfeedissue;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.util.List;

public class PlainRssFetch {

  public static void main(String[] args) {
    String rssFeedUrl = "https://www.reutersagency.com/feed/?post_type=reuters-best";

    try {
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new XmlReader(new URL(rssFeedUrl)));
      List<SyndEntry> entries = feed.getEntries();
      for (SyndEntry syndEntry : entries){
        System.out.println(syndEntry.getTitle());
      }
      System.out.println();
    } catch (Exception e){
      System.out.println("Error occurred");
    }
  }

}
