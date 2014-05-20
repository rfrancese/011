package it.unisa.followteam.support;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RSSItem {
    
   private String title;
   private String description;
   private Date date;
   private String link;
    
   public RSSItem(String title, String description, Date pubDate, String link) {
       this.title          = title;
       this.description = description;
       this.date           = pubDate;
       this.link         = link;
   }
    
   public String getTitle() {
       return title;
   }
    
   public void setTitle(String title) {
       this.title = title;
   }
    
   public String getDescription() {
       return description;
   }
    
   public void setDescription(String description) {
       this.description = description;
   }
    
   public Date getPubDate() {
       return date;
   }
    
   public void setPubDate(Date pubDate) {
       this.date = pubDate;
   }
    
   public String getLink() {
       return link;
   }
    
   public void setLink(String link) {
       this.link = link;
   }
    
   @Override
   public String toString() {
        
       SimpleDateFormat sdf = new SimpleDateFormat("hh:mm - MM/dd/yy");
        
       String result = getTitle() + "   ( " + sdf.format(this.getPubDate()) + " )";
       return result;
   }
   
   
  
}
