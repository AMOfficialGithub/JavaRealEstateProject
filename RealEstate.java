//This is the Main Driver Class

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javafx.scene.control.ScrollBar;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javafx.stage.FileChooser;
import java.util.Scanner;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

public class RealEstate extends Application {
    Button searchButton, addButton, removeButton;
    ListView<String> listView; //Creation of the ListBox
    ImageView imageView;
    //The Instance Variables for the price, bed, city, bath,and yard inputs
    private TextField priceField, bedField, bathField, cityField, yardField;
    private TextArea descriptionField;
    static String HOUSE_INFO_FILE = "houseinfo.txt";
    String selectedLine;
    String[] properties; //Creation of String ArrayList properties
    Stage stage;
    int count; //Instanced variable for incrementing
    //The Main JavaFX Method Line
    public void start(Stage primaryStage) throws Exception {

        //The GUI Outline
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(6, 6, 6, 6));
        grid.setVgap(5); //Setting vertical spacing
        grid.setHgap(6); //Setting horizontal spacing
        stage = primaryStage;
        count = countHouses();
        //Creating ListBox
        listView = new ListView<>();
        listView.getSelectionModel().selectedItemProperty().addListener(e -> {
                if(listView.getSelectionModel().getSelectedItem() != null) {
                    selectedLine = new StringBuffer(listView.getSelectionModel().getSelectedItem()).toString();
                    String[] properties = selectedLine.split(":");

                    String imageFile = String.format("House screenshots/%s.jpg", properties[0]);
                    System.out.println(imageFile);
                    imageView.setImage(new Image(imageFile));

                    descriptionField.setText(properties[6]);
                }
            });

        //The overall font size for the GUI
        Font font = new Font(13);
        Label houseLabel = new Label("House Image");
        houseLabel.setFont(font);
        GridPane.setHalignment(houseLabel, HPos.CENTER);

        //ViewingSize For House Image
        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);

        Label descriptionLabel = new Label("Description"); //For output
        descriptionLabel.setFont(font);
        GridPane.setHalignment(descriptionLabel, HPos.CENTER);

        descriptionField = new TextArea(); //This will output
        descriptionField.setWrapText(true); //this cause the text to wrap in the textarea
        descriptionField.setFont(font);
        descriptionField.setMaxWidth(400);

        Label priceLabel = new Label("Housing Price:");
        priceLabel.setFont(font);
        GridPane.setHalignment(priceLabel, HPos.LEFT);

        priceField = new TextField();
        priceField.setFont(font);
        priceField.setMaxWidth(150);

        //Creating Label for bedrooms
        Label bedLabel = new Label("Beds");
        bedLabel.setFont(font);
        GridPane.setHalignment(bedLabel, HPos.LEFT);

        bedField = new TextField();
        bedField.setFont(font);
        bedField.setMaxWidth(100);

        //Creating Label for baths
        Label bathLabel = new Label("Baths");
        bathLabel.setFont(font);
        GridPane.setHalignment(bathLabel, HPos.LEFT);

        bathField = new TextField();
        bathField.setFont(font);
        bathField.setMaxWidth(100);

        //Creating Label for city
        Label cityLabel = new Label("City");
        cityLabel.setFont(font);
        GridPane.setHalignment(cityLabel, HPos.LEFT);

        cityField = new TextField();
        cityField.setFont(font);
        cityField.setMaxWidth(200);

        //Creating label for Yard
        Label yardLabel = new Label("Yard");
        yardLabel.setFont(font);
        GridPane.setHalignment(yardLabel, HPos.LEFT);

        yardField = new TextField();
        yardField.setFont(font);
        yardField.setMaxWidth(100);

        //Creating the Buttons
        searchButton = new Button("Search");
        GridPane.setHalignment(searchButton, HPos.CENTER);
        searchButton.setOnAction(this::processButtonPress);

        addButton = new Button("Add");
        GridPane.setHalignment(addButton, HPos.CENTER);
        addButton.setOnAction(this::processButtonPress);
        addButton.setPrefWidth(70);

        removeButton = new Button("Delete");
        GridPane.setHalignment(removeButton, HPos.CENTER);
        removeButton.setOnAction(this::processButtonPress);
        removeButton.setPrefWidth(90);

        //Node Placement
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(20);
        root.setVgap(10);
        //root.setStyle("-fx-background-color:green");

        //14 elements
        root.add(houseLabel, 0, 0, 5, 1);
        root.add(imageView, 0, 1, 5, 1);
        root.add(descriptionLabel, 6, 0, 10, 1);
        root.add(descriptionField, 6, 1, 10, 1);
        root.add(priceLabel, 1, 2);
        root.add(priceField, 1, 3);
        root.add(bedLabel, 2, 2);
        root.add(bedField, 2, 3);
        root.add(bathLabel, 4, 2);
        root.add(bathField, 4, 3);
        root.add(cityLabel, 6, 2);
        root.add(cityField, 6, 3);
        root.add(yardLabel, 7, 2);
        root.add(yardField, 7, 3);

        root.add(searchButton, 2, 4);
        root.add(addButton, 5, 4);
        root.add(removeButton, 7, 4);

        root.add(listView, 1, 8, 10, 8);


        ScrollPane scroll = new ScrollPane(root); //Creation of ScrollPane
        //ScrollPane scrolllist= new ScrollPane(); //ScrollPane for listview
        //scrolllist.setContent(listView);
        //scrolllist.setVbarPolicy(ScrollBarPolicy.ALWAYS);

        //The Window Sizing
        Scene scene = new Scene(scroll, 1000, 800);
        primaryStage.setTitle("The Real Estate Viewer");
        primaryStage.setScene(scene);
        scene.getStylesheets().add("theme.css");
        primaryStage.show();
    }

    private void writeToFile(String data) {
        try {
            FileWriter writer = new FileWriter(HOUSE_INFO_FILE, true); //Converts into Stream for Buffer
            BufferedWriter bufferedWriter = new BufferedWriter(writer); //passes write variable into the Buffer
            bufferedWriter.write(data); //Writes string into text file
            bufferedWriter.close(); //enables window to be closed for debugging purposes.Do not remove.
        } catch (Exception e) {
            e.printStackTrace(); //prints if error
        }
    }

    public void processButtonPress(ActionEvent event) {
        listView.getItems().clear();

        try {
            Button clickedButton = (Button) event.getSource();
            String priceString = priceField.getText();
            double price = 0; //Price starts with this

            String inputPrice = null;

            switch (clickedButton.getText()) {
                case "Search":
                if (priceField.getText().length() < 1) return;

                if(priceField.getText().contains(",")) {
                    priceString = priceField.getText().replace(",", "");
                }
                price = Double.parseDouble(priceString);

                inputPrice = NumberFormat.getCurrencyInstance(Locale.US).format(price).substring(1); //This is currency format

                getLinesWithPrice(inputPrice);
                break;
                case "Add":
                if (priceField.getText().length() == 0 || bedField.getText().length() == 0 || bathField.getText().length() == 0 || cityField.getText().length() == 0) {
                    //do nothing
                } else {
                    String newEntry = String.format("\nHouse%d:$%s:%s:%s:%s:%s:%s",(count+1),
                            priceField.getText(), bedField.getText(), bathField.getText(), cityField.getText(), yardField.getText(), descriptionField.getText());

                    writeToFile(newEntry);
                    count++;
                    System.out.println(count);
                }

                String house_file_name = "";
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(stage);
                house_file_name = file.getName();
                System.out.println(house_file_name);
                //The ability to upload a picture while adding to a new entry will go here
                try{
                    //Reading and writing the image to the housescreens folder
                    //here %s need a file name. %s is now house_file_name
                    BufferedImage imageFile=ImageIO.read(file);
                    house_file_name = "House screenshots\\"+house_file_name;
                    System.out.println(house_file_name);
                    ImageIO.write(imageFile,"jpg", new File(house_file_name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

                case "Delete":
                if(selectedLine != null) {
                    deleteLineFromFile();
                    //imageFile.setImage(null);
                }
                break;

            }
        } catch (ParseException | IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    private void deleteLineFromFile() throws IOException {
        File inputFile = new File(HOUSE_INFO_FILE);
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(selectedLine)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();

        Files.delete(Paths.get(HOUSE_INFO_FILE));
        boolean successful = tempFile.renameTo(inputFile);
        selectedLine = null;
        listView.getItems().clear();
    }

    private int countHouses()
    {
        int count = 0;
        Scanner scan = null;
        try
        {
            scan = new Scanner(new File(HOUSE_INFO_FILE));
            while(scan.hasNext())
            {
                scan.nextLine();
                count++;
            }
        }
        catch (Exception ex)
        {
            return 0;
        }
        finally
        {
            scan.close();
        }
        return count;
    }

    private void getLinesWithPrice(String inputPrice) throws IOException, ParseException {
        FileReader file = new FileReader(HOUSE_INFO_FILE); //Importing File
        BufferedReader reader = new BufferedReader(file); //sent as parameter to buffer reader

        //Reading From File
        String line = reader.readLine(); //Keeps reading line after line. Returns null if no text.

        while (line != null && line.length()>1) {
            String[] properties = line.split(":");
            String filePrice = properties[1].substring(1);
            /*House=properities[0]
             * Price=properties[1]
             * Beds = properties[2];
            Baths = properties[3];
            cityName = properties[4];
            Yard Yes or No = properties[5];
             */

            double pricex = NumberFormat.getNumberInstance(Locale.US).parse(filePrice).doubleValue();
            double pricey = NumberFormat.getNumberInstance(Locale.US).parse(inputPrice).doubleValue();
            String status = "Valid";

            if ((pricey-50000) < pricex && pricex < (pricey+50000))
            {    
                //String cityName = cityField.getText();

                if (bedField.getText() != null && !bedField.getText().isEmpty())
                {   
                    if (Integer.parseInt(bedField.getText()) != Integer.parseInt(properties[2]))
                        status = "Invalid";
                } 

                if (bathField.getText() != null && !bathField.getText().isEmpty())
                {   
                    if (Integer.parseInt(bathField.getText()) != Integer.parseInt(properties[3]))
                        status = "Invalid";
                }    

                if (cityField.getText() != null && !cityField.getText().isEmpty())
                {   
                    if (!cityField.getText().equals(properties[4]))
                        status = "Invalid";
                }    

                if (yardField.getText() != null && !yardField.getText().isEmpty())
                {   
                    if (!yardField.getText().equals(properties[5]))
                        status = "Invalid";
                }

                if (status == "Valid") 
                    listView.getItems().add(line);

            }
            line = reader.readLine();
        }
        reader.close();
    }

}

