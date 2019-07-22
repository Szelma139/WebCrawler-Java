/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Szelma
 */
public class Crawler {

    int deepLevel = 1;
    String[] arr;
    String newUrl;
    String t;
    int i;
    int next=0;
int check=-4;
    //String[] arr;
    public Crawler() {
    }
    public int itsTimeToStop = 0;

    public Crawler(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public Set<String> getPagesVisited() {
        return pagesVisited;
    }

    public void setPagesVisited(Set<String> pagesVisited) {
        this.pagesVisited = pagesVisited;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public Document getHtmlDocument() {
        return htmlDocument;
    }

    public void setHtmlDocument(Document htmlDocument) {
        this.htmlDocument = htmlDocument;
    }
    
    public void addToIgnored(String k){
        toIgnore.add(k);
    }
    private static Set<String> toIgnore = new HashSet<String>();
    private Set<String> toVisit = new HashSet<String>();
    private static final String hostExtractorRegexString = "(?:https?://)?(?:www\\.)?(.+\\.)(com|au\\.uk|co\\.in|be|in|uk|org\\.in|pl|org|net|edu|gov|mil)";
    private static final Pattern hostExtractorRegexPattern = Pattern.compile(hostExtractorRegexString);
    public static Set<String> pagesVisited = new HashSet<String>();
    private Set<String> pagesPrepared = new HashSet<String>();
    // private List<String> pagesToVisit = new LinkedList<String>();
    public static int crawlerDepth = 10;

    String currentUrl;
    private Document htmlDocument; // This is our web page, or in other words, our document
    private static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    // private List<String> links = new LinkedList<String>(); // Just a list of URLs
    private Set<String> links = new HashSet<String>();
    // public List<String> getLinks() {
    //return links;
    //}

    public Set<String> getLinks() {
        return links;
    }

    public String returnIP(String currentUrl) throws IOException, URISyntaxException {

        InetAddress address = InetAddress.getByName(new URI(currentUrl).getHost());
        String ip = address.getHostAddress();
        //  System.out.println(ip);
        System.out.println("Wykonuje konwersje adresu url na IP z ADRESU ----  " + currentUrl);

        return ip;
    }

    public static String returnHostname(String currentUrl) throws URISyntaxException, MalformedURLException {

        //  URI uri = new URI(currentUrl);
        URL url = new URL(currentUrl);
        // String hostname = uri.getHost();
        String hostname = url.getHost();
        if (hostname != null) {
            return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
        }
        return hostname;

        // return hostname;
    }

    private String returnTopDomain(String urlString) {

        URL url = null;
        String tldString = null;
        try {
            url = new URL(urlString);
            String[] domainNameParts = url.getHost().split("\\.");
            tldString = domainNameParts[domainNameParts.length - 1];
        } catch (MalformedURLException e) {
        }

        return tldString;
    }

    public static String getDomainName2(String url) {
        if (url == null) {
            return null;
        }
        url = url.trim();
        Matcher m = hostExtractorRegexPattern.matcher(url);
        if (m.find() && m.groupCount() == 2) {
            return m.group(1) + m.group(2);
        } else {
            return null;
        }
    }

    public static String getHost(String url) {
        if (url == null || url.length() == 0) {
            return "";
        }

        int doubleslash = url.indexOf("//");
        if (doubleslash == -1) {
            doubleslash = 0;
        } else {
            doubleslash += 2;
        }

        int end = url.indexOf('/', doubleslash);
        end = end >= 0 ? end : url.length();

        int port = url.indexOf(':', doubleslash);
        end = (port > 0 && port < end) ? port : end;

        return url.substring(doubleslash, end);
    }

    /**
     * Based on :
     * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.3_r1/android/webkit/CookieManager.java#CookieManager.getBaseDomain%28java.lang.String%29
     * Get the base domain for a given host or url. E.g. mail.google.com will
     * return google.com
     *
     * @param host
     * @return
     */
    public static String getBaseDomain(String url) throws URISyntaxException, MalformedURLException {
        String host = getHost(url);
        String substr;

        int startIndex = 0;
        int nextIndex = host.indexOf('.');
        int lastIndex = host.lastIndexOf('.');
        substr = host;
        while (nextIndex < lastIndex) {
            startIndex = nextIndex + 1;
            nextIndex = host.indexOf('.', startIndex);
        }
        if (startIndex > 0) {
            if (host.contains("#")) {
                substr = returnHostname(url);
                return substr;
            } else {
                return host.substring(startIndex);
            }
            //   } else {
            // host.substring(0,host.indexOf("#"));
            //  if (substr.indexOf("#") >= 0){
            // return  host.substring(substr.indexOf("#"),host.length());
            //  }
        } else {
            //System.out.println("HOST SAM " + host);
            if (host.contains("#")) {
                host = returnHostname(url);
            }
            return host;
        }
    }

    public void testProcessing(String url) throws URISyntaxException, IOException {
        try {
            Model model = new Model();
            model.insertFirst(getBaseDomain(this.currentUrl), this.currentUrl, returnIP(this.currentUrl));
            model.insertToEdges(this.currentUrl);
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            ///////////
            //  System.out.println("Adres ip dla url " + currentUrl + " to " + returnIP(this.currentUrl));//
            //    System.out.println("Domena dla url " + currentUrl + " to " + returnHostname(this.currentUrl));//
            //   System.out.println("Received web page at " + url);//
            //     System.out.println("Domena dla url " + currentUrl + " to " + returnTopDomain(this.currentUrl));//
            //       System.out.println("Domena z nowej funkcji dla  " + this.currentUrl + " to " + getDomainName2(this.currentUrl));//
            //                  System.out.println("Domena BASE z nowej funkcji dla " + this.currentUrl + " to " + getBaseDomain(this.currentUrl));//
            System.out.println("---------------------------------------------------------------------------------------");
            Elements linksOnPage = htmlDocument.select("a[href]");
            for (Element link : linksOnPage) {
                if (getBaseDomain(this.currentUrl).equals(getBaseDomain(link.absUrl("href"))) == false) {
                    pagesPrepared.add(link.absUrl("href"));
                    //System.out.println(link.absUrl("href"));
                    this.newUrl = link.absUrl("href").toString();
                }
            }

            model.insertDomainAndUrl(getBaseDomain(this.newUrl), this.newUrl);
            // getBaseDomain("promoceny.pl#iwa_source=side_menu")

            pagesVisited.add(this.newUrl);
            for (Iterator<String> iterator = pagesPrepared.iterator(); iterator.hasNext();) {

                String s = iterator.next();
                //   System.out.println("usuwam teraz" + pagesPrepared.equals(iterator()) + " z listy");
                iterator.remove();

            }

            Iterator<String> iteratorVisited = pagesVisited.iterator();
            String[] pagesPreparedArray = pagesPrepared.toArray(new String[pagesPrepared.size()]); //lista w array wszystkich stron o innej domenie
            String[] pagesVisitedArray = pagesVisited.toArray(new String[pagesVisited.size()]);
            int p = pagesPreparedArray.length;
            int r = pagesVisitedArray.length;
            int i = 0;
//for (i=0 ; i<p;i++)
            //  for(int k=0;k<r;k++)
//{
            //  if (pagesPreparedArray[i].equals(pagesVisitedArray[r]))
            //    pagesPreparedArray = ArrayUtils.removeElement(pagesPreparedArray, i);
            //pagesPreparedArray[i].
//}
///wwww.aaaa.pl
//wwww.bbbb.pl
//wwww.cccc.pl

//while(pagesPreparedArray[i].contains(pagesVisited.iteratorVisited(equals(i)))){
            //  i++;
//}
//wybierz strone pierwsza z array, 
//jesli jest w odwiedzonych to zwieksza +1
//jesli nie jest przetworz ja
            System.out.println();

        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            System.out.println("Jest Exception " + ioe);
        }
        //   FXMLDocumentController controller = new FXMLDocumentController();
//         controller.setCrawlerDepth();
        goToNext2();
    }

    public void processFirstUrl(String url) throws IOException, URISyntaxException {

        Model model = new Model();

        model.insertFirst(returnHostname(this.currentUrl), this.currentUrl, returnIP(this.currentUrl));

        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            ///////////
            System.out.println("Adres ip dla url " + currentUrl + " to " + returnIP(this.currentUrl));
            System.out.println("Domena dla url " + currentUrl + " to " + returnHostname(this.currentUrl));
            System.out.println("Received web page at " + url);
            System.out.println("Domena dla url " + currentUrl + " to " + returnTopDomain(this.currentUrl));
            Elements linksOnPage = htmlDocument.select("a[href]");

            for (Element link : linksOnPage) {
                String p = link.absUrl("href");
                //  if (returnHostname(this.currentUrl).equals(returnHostname(link.absUrl("href")))==false)
                if (returnTopDomain(this.currentUrl).equals(returnTopDomain(link.absUrl("href"))) == false) {
                    pagesPrepared.add(link.absUrl("href"));
                }
            }

            for (String link : pagesPrepared) {
                this.newUrl = link.toString();
                System.out.println(this.newUrl);

            }
            model.insertDomainAndUrl(returnHostname(this.newUrl), this.newUrl); //dla nowego urla z listy insert
            //this.currentUrl=model.retrieveNextUrl();
            pagesVisited.add(this.newUrl);
            for (Iterator<String> iterator = pagesPrepared.iterator(); iterator.hasNext();) {

                String s = iterator.next();
                //   System.out.println("usuwam teraz" + pagesPrepared.equals(iterator()) + " z listy");
                iterator.remove();

            }

        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            System.out.println("Error in out HTTP request " + ioe);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        goToNext();

    }

    public void goToNext() throws IOException, URISyntaxException {

        Model model = new Model();
        FXMLDocumentController controller = new FXMLDocumentController();
// controller.loadDataFromDatabase();
        //String t = returnIP(model.returnFirstNullIp());
        // model.updateIP(returnIP(t), model.returnFirstNullIp());
        System.out.println("adres ip dla " + model.returnFirstNullIp());
        String t = model.returnFirstNullIp();
        //goToNext();
        String k = returnIP(t);
        System.out.println(returnIP(t));
        model.updateIP(k, t);
        URL url1 = new URL(t);
        int i = 0;
        //  returnTopDomain("https://instagram.com/ghost");
        try {
            Connection connection = Jsoup.connect(t).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            ///////////
            System.out.println("PRZETWARZAM TERAZ URL " + t);
            System.out.println("Adres ip dla url " + t + " to " + returnIP(t));
            System.out.println("Domena dla url " + t + " to " + returnHostname(t));
            System.out.println("Domena TOP dla url " + t + " to " + returnTopDomain(t));
            Elements linksOnPage = htmlDocument.select("a[href]");

            for (Element link : linksOnPage) {
                String p = link.absUrl("href");
                //  if (returnHostname(this.currentUrl).equals(returnHostname(link.absUrl("href")))==false)
                if (getBaseDomain(t).equals(getBaseDomain(link.absUrl("href"))) == false) {
                    pagesPrepared.add(link.absUrl("href"));
                    System.out.println(link.absUrl("href"));
                }
            }

            String[] arrVisited = pagesVisited.toArray(new String[pagesVisited.size()]);
            arr = pagesPrepared.toArray(new String[pagesPrepared.size()]);
            // if(pagesPrepared.equals(pagesVisited))
            for (String link : pagesPrepared) {
                this.newUrl = link.toString();
                System.out.println("Nowo znaleziony url to " + this.newUrl);

            }
            // pagesPreparedArray[i];
            System.out.println(model.compareUrl(this.newUrl));
            if (model.compareUrl(arr[i]) == false) {
                i++;
            }
            System.out.println("arr wynosi teraz -- >>  " + arr[i]);
            
            if (arr[i] != null) {
                model.insertDomainAndUrl(getBaseDomain(arr[i]), arr[i]); //dla nowego urla z listy insert arr
            }
            //this.currentUrl=model.retrieveNextUrl();
            for (Iterator<String> iterator = pagesPrepared.iterator(); iterator.hasNext();) {

                String s = iterator.next();
                iterator.remove();
                //      
            }

        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            System.out.println("Error in out HTTP request " + ioe);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        goToNext();

    }

    public boolean checkCurrentUrl(String url) throws IOException {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                System.out.println("\n**Visiting** Received web page at " + url);
                return true;
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            return false;
        }
        return false;
    }

    public void goToNext2() throws IOException, URISyntaxException {
//int check=0;
        Model model = new Model();
        FXMLDocumentController controller = new FXMLDocumentController();
        controller.loadDataFromDatabase();
        //String t = returnIP(model.returnFirstNullIp());
        // model.updateIP(returnIP(t), model.returnFirstNullIp());
        
        System.out.println("adres ip dla " + model.returnFirstNullIp());
        String t = model.returnFirstNullIp();  //temp url
        //goToNext();
        String k = returnIP(t);
        System.out.println(returnIP(t));
        model.updateIP(k, t);
        model.insertToEdges(t);
        Connection connection = null;
        URL url1 = new URL(t);
        //pagesPrepared = null;
        //arr = null;
        i = 0;
        model.addToVisited();
        //  int i = 0;
        //  returnTopDomain("https://instagram.com/ghost");
//if (this.crawlerDepth-1>0)
        try {
             // while(crawlerDepth-1>0)
            //  {
  //  if (this.crawlerDepth-1>0)
        //  {
            connection = Jsoup.connect(t).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            ///////////
            System.out.println("PRZETWARZAM TERAZ URL " + t);
            System.out.println("Adres ip dla url " + t + " to " + returnIP(t));
            System.out.println("Domena dla url " + t + " to " + returnHostname(t));
            System.out.println("Domena TOP dla url " + t + " to " + returnTopDomain(t));
             this.htmlDocument = htmlDocument;
           // if(connection.response().statusCode() == 200) {
            Elements linksOnPage = htmlDocument.select("a[href]");

            for (Element link : linksOnPage) {
                //String p = link.absUrl("href");
                if (getBaseDomain(t).equals(getBaseDomain(link.absUrl("href"))) == false) {
                    //Pattern p = Pattern.compile("href=\"((http://|https://|www).*?)\"", Pattern.DOTALL);
                    Pattern p = Pattern.compile("/(((http|ftp|https):\\/{2})+(([0-9a-z_-]+\\.)+(aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|cz|de|dj|dk|dm|do|dz|ec|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mn|mn|mo|mp|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|nom|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ra|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sj|sk|sl|sm|sn|so|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw|arpa)(:[0-9]+)?((\\/([~0-9a-zA-Z\\#\\+\\%@\\.\\/_-]+))?(\\?[0-9a-zA-Z\\+\\%@\\/&\\[\\];=_-]+)?)?))\\b/imuS");
                    Matcher m = p.matcher(link.absUrl("href"));
                    //matcher i pattern nieuzytkowany
                    // String p = link.absUrl("href");
                    //System.out.println("wypisuje " + model.compareUrl(link.absUrl("href")));
                    // model.compareUrl(link.absUrl("href"));
                    //if (model.compareUrl(link.absUrl("href")) == true) {
                    //  pagesPrepared.add(link.absUrl("href"));
                    //}
                    //  while(m.find()) {

                    if (pagesVisited != null) {
                        if (!pagesVisited.contains(link.absUrl("href"))) {
                            if(!toIgnore.contains(link.absUrl("href")))
                            // if (model.compareUrl(link.absUrl("href"))==true)
                            if (controller.isValid(link.absUrl("href")) != false) {
                                pagesPrepared.add(link.absUrl("href"));
                                System.out.println("Dodaje link do pagesPrepared" + link.absUrl("href"));
                            }
                        }
                    }

                }
            }
//}   // matcher
            // String[] arrVisited = pagesVisited.toArray(new String[pagesVisited.size()]);
            arr = pagesPrepared.toArray(new String[pagesPrepared.size()]);
                    checkArrayRange(arr.length);
            // if(pagesPrepared.equals(pagesVisited))
            if ((arr.length != 0)) {
                for (String link : pagesPrepared) {
                    this.newUrl = link.toString();
                    System.out.println("Nowo znaleziony url to " + this.newUrl);

                }
                // pagesPreparedArray[i];
                // System.out.println(model.compareUrl(this.newUrl));
                //   System.out.println("arr od i wynosi" + arr[i] + " ---- natomiast arr wynosi" + arr);
                //   if (arr[i] != null && arr != null) {
                //if (arr.length !=0){
                while (model.compareUrl(arr[i]) == false) {
                    i++;
                    System.out.println("arr dla i = -- >>  " + i + " wynosi" + arr[i]);
                }
                System.out.println("arr wynosi teraz -- >>  " + arr[i]);
                model.insertDomainAndUrl(getBaseDomain(arr[i]), arr[i]); //dla nowego urla z listy insert arr
    // --this.crawlerDepth;  
                for (Iterator<String> iterator = pagesPrepared.iterator(); iterator.hasNext();) {

                    String s = iterator.next();
                    iterator.remove();

                }

                //}
        
            }
               
            //  } //w razie czego to wywalic
          //  } //connection if
           // } //if do scopa
        } catch (IOException ioe) {

            System.out.println("Error in out HTTP request " + ioe);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //while(crawlerDepth-1>0)
      //  if (crawlerDepth-1>0)
      //| ((this.crawlerDepth-1)>0)) 
 System.out.println("=--------------------------------------------------------------=");
      System.out.println(toIgnore);
        if (arr.length != 0)
      //  if (check !=0)
    //  if ((this.check>0))
              //| this.crawlerDepth >0)
                {
            goToNext2();
        } else {
            controller.infoBox("Nie ma wiecej adresow url o innej domenie niz " + getBaseDomain(t), "Dla strony -> " + t, "Brak adresow o innej domenie na podanej stronie.");
        }

    }

    
    private boolean checkArrayRange(int l){
        if (l>0)
        {
            this.next=l;
            this.check = l;
        return true;
        }
                else {
                this.next=0;
        return false;
                     }
    }
}
