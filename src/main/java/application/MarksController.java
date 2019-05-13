package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import application.DashBoardController.Person_Marks;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Pair;

public class MarksController 
{
	public static String in="INSTRUCTIONS  FOR USAGE:\r\n" + 
			"\r\n" + 
			"  #TO LOAD A SPREADSHEET: \r\n" + 
			"  1) CLICK THE LOAD SPREADSHEET\r\n" + 
			"      BUTTON\r\n" + 
			"  2) LOAD THE REQUIRED FILE FROM \r\n" + 
			"     THE SYSTEM EXPLORER\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"  #TO SAVE SPREADSHEET:\r\n" + 
			"  1) CLICK THE SAVE SPREADSHEET\r\n" + 
			"      BUTTON\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"  #TO SAVE AND SYNC\r\n" + 
			"     ONLINE:\r\n" + 
			"  1) CLICK THE SAVE BUTTON. THE  \r\n" + 
			"      DATA GETS SYNCED TO FIREBASE\r\n" + 
			"  2) CLICK THE  SYNC\r\n" + 
			"       BUTTON TO TRANSFER THE DATA \r\n" + 
			"       INTO THE OFFICIAL WORD \r\n" + 
			"       DOCUMENT\r\n" + 
			"\r\n" + 
			"";
	
	String tfsem = "";
	String tfsec = "";

	@FXML
	TextField semester=new TextField();
	@FXML
    TextField section=new TextField();
	@FXML
	TitledPane tpma;
	@FXML 
	AnchorPane ap_marks, ap;
	@FXML
	Label in2;
	@FXML
	String rootpath = "C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM";
	@FXML
	VBox utility_marks;
	String coursecode ="";
	Boolean exp=false;
	
	@FXML
	private TableView<Person_Marks> table_marks;
	
	TableColumn usnCol2,nameCol2,marksCol,newCol;
	
	 private ObservableList<Person_Marks> data1 =
		        FXCollections.observableArrayList();
	@FXML
	Button savefiremarksbtn, loadfiremarksbtn, loadmarksbtn, savemarksbtn;
	
	 
    ArrayList<ArrayList<String>> marks;
	ArrayList<String> names;
	
	 Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	    ArrayList<String> usns = new ArrayList<>();
	    ArrayList<String> name = new ArrayList<>();
	    static ArrayList<String> studdat = new ArrayList<String>();
	    
	    
	    public void initialize() throws IOException
		{		
			
			
			 Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		       //header.setPrefWidth(screenBounds.getWidth());
		       utility_marks.setPrefHeight(screenBounds.getHeight());
		      // vbox_nav.setPrefHeight(screenBounds.getHeight());
		        
		       	in2.setText(in);
		       	in2.setWrapText(true);
		        in2.setStyle("-fx-font-family: \"Times New Roman \"; -fx-font-size: 20; -fx-text-fill: white; -fx-background-color:#6464a5;");
		        int width = (int) screenBounds.getWidth();
		        int utilsize = (width/100)*20;
		        int lab_size = (width/100)*60;
		        utility_marks.setPrefWidth(utilsize);
		        
		        table_marks.setPrefWidth(lab_size);
		        table_marks.setPrefHeight(screenBounds.getHeight());
		        utility_marks.setPrefHeight(screenBounds.getHeight());
		        ap_marks.setPrefWidth(lab_size);
		        ap_marks.setPrefHeight(screenBounds.getHeight());

		        
		        loadfiremarksbtn.setPrefWidth(utilsize);
		        savefiremarksbtn.setPrefWidth(utilsize);
		        savemarksbtn.setPrefWidth(utilsize);
		        loadmarksbtn.setPrefWidth(utilsize);
		        
		        table_marks.setEditable(true);
		        usnCol2 = new TableColumn("USN");
		        usnCol2.setMinWidth(100);
		        usnCol2.setCellValueFactory(
		                new PropertyValueFactory<Person_Marks, String>("usn"));
		       
		        nameCol2 = new TableColumn("NAME");
		        nameCol2.setMinWidth(100);
		        nameCol2.setCellValueFactory(
		                new PropertyValueFactory<Person_Marks, String>("name"));

		        marksCol = new TableColumn("Marks[50]");
		        marksCol.setMinWidth(100);
		        marksCol.setCellValueFactory(
		                new PropertyValueFactory<Person_Marks, String>("marks"));
		        marksCol.setCellFactory(TextFieldTableCell.forTableColumn());
		        marksCol.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person_Marks, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person_Marks, String> t) {
		        ((Person_Marks) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setMarks(t.getNewValue());
		        }
		        }
		        );
		        tpma.setExpanded(false);
		        table_marks.setItems(data1);
		        table_marks.getColumns().addAll(usnCol2,nameCol2, marksCol);
		}
	    
	    public void openMarks(ActionEvent e)
	    {
	    	if(!exp==true)
	    	{
	    	exp =true;
	    	tpma.setExpanded(true);
	    	}
	    	else
	    	{
	    		exp=false;
	    		tpma.setExpanded(false);
	    	}
	    }
	    public  void LoadMarks(ActionEvent e)throws IOException
		{
	    	 studdat.clear();
			    tfsem = semester.getText().toString();
			    tfsem = tfsem.toUpperCase();
			    tfsec = section.getText().toString();
			    tfsec = tfsec.toUpperCase();
			    studdat.add(tfsem);
			    studdat.add(tfsec);

		   
			
		    
		    table_marks.getColumns().clear();
		    table_marks.getColumns().addAll(usnCol2, nameCol2, marksCol);
		    table_marks.setItems(data1);
			
			InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+".xls");
			HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row; 
			data1.clear();
			for(int i=1;i<sheet.getPhysicalNumberOfRows();i++)
			{
			
			HSSFRow row1 = sheet.getRow(i);
			Iterator cells = row1.cellIterator();
			while (cells.hasNext())
			{
			
			cells.next();
			}
			String usn = sheet.getRow(i).getCell(0).toString();
			String name = sheet.getRow(i).getCell(1).toString();
			String marks50 = sheet.getRow(i).getCell(2).toString();
			
			
			
			data1.add(new Person_Marks(usn,name,marks50));
			
				
		}
			
			table_marks.getColumns().clear();
			table_marks.getColumns().addAll(usnCol2, nameCol2, marksCol);
			table_marks.setItems(data1);
			
		}
		
		public  void SaveMarks(ActionEvent e)throws IOException
		{
			InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+".xls");
			HSSFWorkbook  workbook = new HSSFWorkbook(ExcelFileToRead);
	        HSSFSheet spreadsheet = workbook.getSheetAt(0);


		    
	        int i=1;
	        for(Person_Marks dsce: data1)
			{
				if(!dsce.getMarks().equals(""))
				{
					spreadsheet.getRow(i).createCell(2).setCellValue(dsce.getMarks());
				System.out.println(dsce.getMarks());
				int mark = Integer.parseInt((dsce.getMarks().toString()));
				
				
				int marks = (int) Math.round(mark*0.2);;
				
				spreadsheet.getRow(i).createCell(3).setCellValue(marks+"");
				}
				i++;
				
			}

		    

		    FileOutputStream fileOut = new FileOutputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+".xls");
		    workbook.write(fileOut);
		    fileOut.close();
		    
		    Alert alert=new Alert(AlertType.INFORMATION);
		    alert.setTitle("Information Dialog");
		    alert.setHeaderText(null);
		    alert.setContentText("Data Saved!");
		    alert.showAndWait();
			return;
		}
		
		public void MarksCreate(ActionEvent e) throws IOException
    	{
			ArrayList<DataSnapshot> Userlist = new ArrayList<DataSnapshot>(); 
			
		try {
          final CountDownLatch latch1 = new CountDownLatch(1);
          DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
          DatabaseReference ref2;    
           ref2 = ref1.child("Marks/sem_2/");


           ref2.addListenerForSingleValueEvent(
        		   new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                //ArrayList<Object> Userlist = new ArrayList<Object>();   
                ArrayList<ArrayList<String>> big_arr = new ArrayList<ArrayList<String>>();
               	                      for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                      Userlist.add(dsp); 
                     
                    }
                //big_arr.add(Userlist);
                
     				 // System.out.println(Userlist.get(0)+"dsad"+Userlist.size());
     				     
                                            latch1.countDown();
    				     }

    		        	  public void onCancelled(DatabaseError error) {
    		        		  latch1.countDown();
    		        		  
    		        	  }
    		        	});
    		        	 latch1.await();
    			   } 
    			 catch (InterruptedException en) {
    			        en.printStackTrace();
    			    }
    			
    	ArrayList<ArrayList<String>> big = new ArrayList<ArrayList<String>>();
    	ArrayList<String> smol = new ArrayList<String>();
    	
    	for(DataSnapshot d: Userlist.get(0).getChildren())
    	{
    		smol = new ArrayList<String>();
    		FireData fir = d.getValue(FireData.class);
    		System.out.println(fir.getAtt());
    		smol.addAll(Arrays.asList(fir.getAtt().split(",")));
    		big.add(smol);
    		smol = new ArrayList<String>();
    		smol.addAll(Arrays.asList(fir.getPerc().split(",")));
    		big.add(smol);
    	}

    	System.out.println(big);
    		Marks(big);
    	}
    	
    	public void Marks(ArrayList<ArrayList<String>> big) throws IOException
    	{
    		       
    		XWPFDocument docX2 = new XWPFDocument();
    		 		 
    		      XWPFParagraph paragraph = docX2.createParagraph();
    		      paragraph.setAlignment(ParagraphAlignment.CENTER);	      
    		      XWPFRun paragraphOneRunOne = paragraph.createRun();
    		      paragraphOneRunOne.setBold(true);
    		      paragraphOneRunOne.setText("DAYANANDA SAGAR COLLEGE OF ENGINEERING");
    		      paragraphOneRunOne.addBreak();
    		      
    		     
    		      XWPFRun paragraphOneRunTwo = paragraph.createRun();
    	          paragraphOneRunTwo.setBold(true);
    		      paragraphOneRunTwo.setText("DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING");
    		      paragraphOneRunTwo.addBreak();
    		      paragraphOneRunTwo.addBreak();
    		      
    		      
    		      XWPFRun paragraphOneRunThree = paragraph.createRun();
    		      paragraphOneRunThree.setBold(true);
    		      paragraphOneRunThree.setText("SECOND TEST MARKS DISPLAY");
    		      paragraphOneRunThree.addBreak();
    		      
    		      XWPFRun paragraphOneRunFour = paragraph.createRun();
    		      paragraphOneRunFour.setBold(true);
    		      paragraphOneRunFour.setText("(Session: Jan 2019-May 2019)");
    		      paragraphOneRunFour.addBreak();
    		      paragraphOneRunFour.addBreak();
    		      paragraphOneRunFour.addBreak();
    		      
    		      XWPFParagraph paragraph1 = docX2.createParagraph();
    		      paragraph1.setAlignment(ParagraphAlignment.LEFT);
    		      
    		      XWPFRun paragraphTwoRunOne = paragraph1.createRun();
    		      paragraphTwoRunOne.setBold(true);
    		      paragraphTwoRunOne.setText("Class: 5th A                                                                                                                                    Max. Marks:10");
    		      
    		     
    		      //create table
    		      XWPFTable table = docX2.createTable();
//    		      table.setWidth(3*1440);

    		      //create first row
    		      XWPFTableRow tableRowOne = table.getRow(0);
    		      table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(1000));
    		      tableRowOne.getCell(0).setText("SL.#");
    		      tableRowOne.addNewTableCell().setText("USN");
    		      tableRowOne.addNewTableCell().setText("Subject");
    		      tableRowOne.addNewTableCell().setText("ME");
    		      tableRowOne.addNewTableCell().setText("CN");
    		      tableRowOne.addNewTableCell().setText("DBMS");
    		      tableRowOne.addNewTableCell().setText("SE");
    		      tableRowOne.addNewTableCell().setText("ATFL");
    		      tableRowOne.addNewTableCell().setText("AIAT");
    		      tableRowOne.addNewTableCell().setText("ADF");
//    		      tableRowOne.addNewTableCell().setText("P7");
//    		      tableRowOne.addNewTableCell().setText("P8");
//    		      tableRowOne.addNewTableCell().setText("P9");
//    		      tableRowOne.addNewTableCell().setText("P10");
//    		      tableRowOne.addNewTableCell().setText("P11");
//    		      tableRowOne.addNewTableCell().setText("P12");
    		    
    		     
    		   int[] cols = {8000,20000, 20000, 10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000}; 
    		     
    		      for (int i = 0; i < table.getNumberOfRows(); i++) {
    		    	    XWPFTableRow row = table.getRow(i);
    		    	    int numCells = row.getTableCells().size();
    		    	    for (int j = 0; j < numCells; j++)
    		    	    {
    		    	        XWPFTableCell cell = row.getCell(j);
    		    	        CTTblWidth cellWidth = cell.getCTTc().addNewTcPr().addNewTcW();
    		    	        CTTcPr pr = cell.getCTTc().addNewTcPr();
    		    	        pr.addNewNoWrap();
    		    	        cellWidth.setW(BigInteger.valueOf(cols[j]));
    		    	        
    		    	        
    		    	    } 
    		    	}
    		      
    		      InputStream ExcelFileToRead = new FileInputStream("D:\\Book1.xls");
    			  	HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
    			  	HSSFSheet sheet = wb.getSheetAt(0);
    			  	HSSFRow row; 
    			  	
    			  	ArrayList<String> names = new ArrayList<String>();
    			  	ArrayList<String> usns = new ArrayList<String>();
    			  	
    			  	for(int i=0;i<sheet.getPhysicalNumberOfRows();i++)
    			  	{
    			  		
    			  	
    			  	String usn = sheet.getRow(i).getCell(0).toString();
    			  	String name = sheet.getRow(i).getCell(1).toString();
    			    
    			  	names.add(name);
    			  	usns.add(usn);
    			      
    			  	}
    			  	
    			  	for(int i=0;i<names.size();i++)
    			  	{
    			  		table.createRow().getCell(0).setText(String.valueOf(i+1));
    			  		table.getRow(i+1).getCell(1).setText(usns.get(i));
    			  		table.getRow(i+1).getCell(2).setText(names.get(i));
    			  		System.out.println(i);
    			  		if(i<big.get(0).size())
    			  		{
    			  		for(int k=0;k<big.size();k++)
    			  		{
    			  			table.getRow(i+1).getCell(k+3).setText(big.get(k).get(i).toString());
    			  		}
    			  		}
    			  	}
    			  	
    		/*System.out.println("Enter no. of students");
    		int n= Integer.parseInt(in.readLine());
    		//ChangeOrientation obj= new ChangeOrientation();

    		for( int i=0;i<n;i++)
    		      {XWPFTableRow tableRowNext = table.createRow();
    		      
    		      tableRowNext.getCell(0).setText(Integer.toString(i));
    		      tableRowNext.getCell(1).setText("idk");
    		      tableRowNext.getCell(2).setText("idk");
    		      tableRowNext.getCell(3).setText("idk");
    		      tableRowNext.getCell(4).setText("idk");
    		      tableRowNext.getCell(5).setText("idk");
    		      tableRowNext.getCell(6).setText("idk");
    		      tableRowNext.getCell(7).setText("idk");
    		      tableRowNext.getCell(8).setText("idk");
    		      tableRowNext.getCell(9).setText("idk");
    		      }
    		      
    		      

    */
    		FileOutputStream fileOut = new FileOutputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\mark.docx");
    	      docX2.write(fileOut);
    	      fileOut.close();
    			            
    		            System.out.println(".docx written successully");
    		              
    	}
        
        
        
    	public static class Person_Marks {
    	  	 
    	    private final SimpleStringProperty usn;
    		private final SimpleStringProperty name;
    	    private final SimpleStringProperty marks;
    	    
    	    
    	    private Person_Marks(String string,String string1, String string2) {
    	    	this.usn = new SimpleStringProperty(string);
    	    	this.name =new SimpleStringProperty(string1);
    	        this.marks =new SimpleStringProperty(string2);
    	    }
    	    
    	    

    		public String getUsn() {
    	        
    	        return usn.get();
    	    }
    	    
    	    public void setUsn(String u) {
    	       usn.set(u);
    	    }

    	    public String getName() {
    	        
    	        return name.get();
    	    }

    	    
    	    public void setName(String u) {
    	        name.set(u);
    	    }

    	    public String getMarks() {
    	    	
    	    	return marks.get();
    	    }

    	    public void setMarks(String u) {
    	        marks.set(u);
    	       
    	    }
    	}


	    
}
