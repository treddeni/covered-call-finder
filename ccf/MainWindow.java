package ccf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class MainWindow 
{
   protected Shell shlCoveredCallshortPut;
   private Text maxDistanceTB;
   private Text minDaysTB;
   private Text maxDaysTB;
   private Text minProfitTB;
   private Text minProtectionTB;
   private Text symbolsTB;
   private Table table;
   TableData tableData;

   public static void main(String[] args) 
   {
      try {
         MainWindow window = new MainWindow();
         window.open();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void open() 
   {
      Display display = Display.getDefault();
      createContents();
      shlCoveredCallshortPut.open();
      shlCoveredCallshortPut.layout();
      while (!shlCoveredCallshortPut.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
   }
   
   protected void drawResultsTable(ArrayList<OptionQuote> trades)
   {
      try
      {
         DecimalFormat formatTwo = new DecimalFormat("#.##");
         DecimalFormat formatFour = new DecimalFormat("#.####");
         
         table.clearAll();
         table.removeAll();
         
         if(trades.size() > 0)
         {
            for(OptionQuote trade : trades)
            {
               TableItem item = new TableItem(table, SWT.NONE);
               item.setText(0, trade.getTickerSymbol());
               item.setText(1, formatTwo.format(trade.getUnderlyingLast()));
               item.setText(2, trade.getType());
               item.setText(3, formatTwo.format(trade.getStrike()));
               item.setText(4, Integer.toString(trade.getDaysToExpiration()));
               item.setText(5, formatFour.format(trade.getProfitAPR()));
               item.setText(6, formatFour.format(trade.getProtection()));
            }
         }
         else
         {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, "None Found");
         }
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }

   protected void createContents() 
   {
      shlCoveredCallshortPut = new Shell();
      shlCoveredCallshortPut.setSize(1223, 885);
      shlCoveredCallshortPut.setText("Covered Call/Short Put Finder");
      
      Label lblFilters = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblFilters.setBounds(10, 10, 55, 38);
      lblFilters.setText("Filters:");
      
      Label lblMinDistance = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblMinDistance.setAlignment(SWT.RIGHT);
      lblMinDistance.setBounds(10, 54, 253, 30);
      lblMinDistance.setText("Max Distance from the Money:");
      
      Label lblMinDaysRemaining = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblMinDaysRemaining.setAlignment(SWT.RIGHT);
      lblMinDaysRemaining.setBounds(90, 93, 173, 30);
      lblMinDaysRemaining.setText("Min Days Remaining:");
      
      Label lblMaxDaysRemaining = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblMaxDaysRemaining.setAlignment(SWT.RIGHT);
      lblMaxDaysRemaining.setBounds(90, 129, 173, 27);
      lblMaxDaysRemaining.setText("Max Days Remaining:");
      
      maxDistanceTB = new Text(shlCoveredCallshortPut, SWT.BORDER);
      maxDistanceTB.setText("0.10");
      maxDistanceTB.setBounds(269, 51, 98, 33);
      
      minDaysTB = new Text(shlCoveredCallshortPut, SWT.BORDER);
      minDaysTB.setText("20");
      minDaysTB.setBounds(269, 90, 98, 30);
      
      maxDaysTB = new Text(shlCoveredCallshortPut, SWT.BORDER);
      maxDaysTB.setText("50");
      maxDaysTB.setBounds(269, 126, 98, 30);
      
      Label lblMinProfitApr = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblMinProfitApr.setAlignment(SWT.RIGHT);
      lblMinProfitApr.setText("Min Profit APR:");
      lblMinProfitApr.setBounds(392, 54, 129, 30);
      
      Label lblMinProtection = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblMinProtection.setAlignment(SWT.RIGHT);
      lblMinProtection.setText("Min Protection:");
      lblMinProtection.setBounds(392, 93, 129, 30);
      
      minProfitTB = new Text(shlCoveredCallshortPut, SWT.BORDER);
      minProfitTB.setText("0.10");
      minProfitTB.setBounds(527, 51, 98, 33);
      
      minProtectionTB = new Text(shlCoveredCallshortPut, SWT.BORDER);
      minProtectionTB.setText("0.05");
      minProtectionTB.setBounds(527, 90, 98, 33);
      
      Button findButton = new Button(shlCoveredCallshortPut, SWT.NONE);
      findButton.addSelectionListener(new SelectionAdapter() 
      {
         @Override
         public void widgetSelected(SelectionEvent arg0)
         {
            try
            {
               tableData = new TableData();
               String[] symbols = symbolsTB.getText().split("\r\n");
               tableData.setTrades(TK.findTrades(symbols, Double.parseDouble(maxDistanceTB.getText()), Integer.parseInt(minDaysTB.getText()), Integer.parseInt(maxDaysTB.getText()), Double.parseDouble(minProfitTB.getText()), Double.parseDouble(minProtectionTB.getText())));
               drawResultsTable(tableData.getTrades());
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
         }
      });
      findButton.setBounds(850, 34, 161, 65);
      findButton.setText("Find");
      
      Label lblSymbols = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblSymbols.setBounds(10, 184, 98, 30);
      lblSymbols.setText("Symbols:");
      
      symbolsTB = new Text(shlCoveredCallshortPut, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
      symbolsTB.setBounds(10, 220, 119, 600);
      
      table = new Table(shlCoveredCallshortPut, SWT.BORDER | SWT.FULL_SELECTION);
      table.setBounds(135, 220, 1056, 600);
      table.setHeaderVisible(true);
      table.setLinesVisible(true);
      
      TableColumn tblclmnSymbol = new TableColumn(table, SWT.NONE);
      tblclmnSymbol.setWidth(81);
      tblclmnSymbol.setText("Symbol");
      
      TableColumn tblclmnLast = new TableColumn(table, SWT.NONE);
      tblclmnLast.setWidth(68);
      tblclmnLast.setText("Last");
      
      TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
      tblclmnType.setWidth(79);
      tblclmnType.setText("Type");
      
      TableColumn tblclmnStrike = new TableColumn(table, SWT.NONE);
      tblclmnStrike.setWidth(86);
      tblclmnStrike.setText("Strike");
      
      TableColumn tblclmnDays = new TableColumn(table, SWT.NONE);
      tblclmnDays.setWidth(78);
      tblclmnDays.setText("Days");
      
      TableColumn tblclmnProfitApr = new TableColumn(table, SWT.NONE);
      tblclmnProfitApr.addSelectionListener(new SelectionAdapter() 
      {
         @Override
         public void widgetSelected(SelectionEvent arg0) 
         {
            drawResultsTable(tableData.sortTrades(TableData.PROFIT));
         }
      });
      tblclmnProfitApr.setWidth(125);
      tblclmnProfitApr.setText("Profit APR");
      
      TableColumn tblclmnProtection = new TableColumn(table, SWT.NONE);
      tblclmnProtection.addSelectionListener(new SelectionAdapter() 
      {
         @Override
         public void widgetSelected(SelectionEvent arg0) 
         {
            drawResultsTable(tableData.sortTrades(TableData.PROTECTION));
         }
      });
      tblclmnProtection.setWidth(121);
      tblclmnProtection.setText("Protection");
      
      Label lblResults = new Label(shlCoveredCallshortPut, SWT.NONE);
      lblResults.setText("Results:");
      lblResults.setBounds(135, 184, 98, 30);
   }
}
