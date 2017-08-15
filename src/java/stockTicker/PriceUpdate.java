package stockTicker;

import java.io.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet(urlPatterns = {"/stream"})
public class PriceUpdate extends HttpServlet {

    final Stock stock = new Stock();

    @Override
    public void init(ServletConfig config) {
        stock.start();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/event-stream");
        response.flushBuffer();
        Logger.getGlobal().log(Level.INFO, "Beginning update stream.");

        try (PrintWriter out = response.getWriter()) {
            while (!Thread.interrupted())
                synchronized (stock) {
                    stock.wait();

                    out.print("");
                    out.println("data: "+ stock.time +" price "+ stock.price);
                    out.println();
                    out.flush();
                }
        } catch (InterruptedException e) {
            Logger.getGlobal().log(Level.INFO, "Terminating updates.");
            response.setStatus(HttpServletResponse.SC_GONE);
        }
    }

    @Override
    public void destroy() {
        stock.interrupt();
    }
}
