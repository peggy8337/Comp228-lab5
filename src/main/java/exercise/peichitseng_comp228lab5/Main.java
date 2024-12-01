package exercise.peichitseng_comp228lab5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Main extends Application {

    Stage window;
    private static Connection con = null;


    @Override
    public void start(Stage primaryStage) {
        try {
            window=primaryStage;
            window.setTitle("Game Score Management");



            TabPane tabPane = new TabPane();

            //Tab: add game
            Tab tab1=new Tab("New Game");
            tab1.setClosable(false);
            tab1.setContent(layout1());

            //Tab: add player
            Tab tab2=new Tab("New player");
            tab2.setClosable(false);
            tab2.setContent(layout2());

            //Tab: update player
            Tab tab3=new Tab("Update player");
            tab3.setClosable(false);
            tab3.setContent(layout3());

            //Tab: add score
            Tab tab4=new Tab("Add player score");
            tab4.setClosable(false);
            tab4.setContent(layout4());

            //Tab: Display Info
            Tab tab5=new Tab("Report");
            tab5.setClosable(false);
            tab5.setContent(layout5());

            tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);

            Scene scene=new Scene(tabPane);



            //default
            window.setScene(scene);
            window.setMinWidth(600);
            window.setMaxWidth(600);
            window.setMinHeight(600);
            window.setMaxHeight(600);
            window.show();



        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        initializeConnection();
        launch(args);
        closeConnection();
    }

    public Pane layout1() {

        VBox layout=new VBox(50);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(50, 0,0,0));

        HBox Lay_GameTitle=new HBox(10);
        Lay_GameTitle.setAlignment(Pos.TOP_CENTER);

        //Label GameTitle
        Label L_GameTitle = new Label("Game Name:");
        L_GameTitle.setMinHeight(40);

        //TextField GameTitle
        TextField T_GameTitle= new TextField();
        T_GameTitle.setMinHeight(40);

        Lay_GameTitle.getChildren().addAll(L_GameTitle, T_GameTitle);


        layout.setMargin(Lay_GameTitle,new Insets(5));
        layout.setStyle("-fx-font-size: 15px;");


        //Button
        Button B_AddGame=new Button("Insert");
        B_AddGame.setStyle("-fx-font-size: 15px;");

        TableView<GameModel> table=new TableView<GameModel>();
        DisplayGame(table);
        //Column: GAME ID
        TableColumn<GameModel, Integer> firstColumn= new TableColumn<>("ID");
        firstColumn.setCellValueFactory(new PropertyValueFactory<GameModel, Integer>("GameID"));
        firstColumn.setMinWidth(300); // Set minimum width

        //Column: Game Title
        TableColumn<GameModel, String> secondColumn=new TableColumn<>("Title");
        secondColumn.setCellValueFactory(new PropertyValueFactory<GameModel, String>("GameTitle"));
        secondColumn.setMinWidth(300); // Set minimum width

        table.getColumns().addAll(firstColumn, secondColumn);


        B_AddGame.setOnAction(e -> {
            AddGame(T_GameTitle);
            DisplayGame(table);

        });
        layout.getChildren().addAll(Lay_GameTitle, B_AddGame,table);





        return layout;


    }


    private TableView<GameModel> DisplayGame(TableView<GameModel> table) {




        String sql="select game_id, game_title from PEICHITSENG_GAME";
        ObservableList<GameModel> data = FXCollections.observableArrayList();
        try(
                PreparedStatement pstmt=con.prepareStatement(sql)) {

            table.setItems(null);
            ResultSet rs=pstmt.executeQuery();

            while(rs.next()) {

                int game_id=rs.getInt("game_id");
                String title=rs.getString("game_title");
                data.add(new GameModel(game_id,title));
            }

            table.setItems(data);


        }
        catch(Exception error) {
            error.printStackTrace();
            Alert err=NewError(error.getMessage());
            err.show();
        }

        return table;
    }


    //Tab: add player
    public Pane layout2() {

        VBox layout= new VBox();

        GridPane grid = new GridPane();
//		grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(50, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(15);

        //grid constraints
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1,col2);


        //set fixed Row Height
        RowConstraints fixedRowHeight = new RowConstraints();
        fixedRowHeight.setMinHeight(40); // Minimum height of the row

        //total of 6 row for text fields
        for (int i = 0; i < 6; i++) { // Adjust the number of rows as needed
            grid.getRowConstraints().add(fixedRowHeight);
        }


        //Warning area(placed bottom-down)
        HBox HBox_Msg= new HBox();
        HBox_Msg.setPadding(new Insets(20,0,0,0));
        Label Message= new Label();
        HBox_Msg.getChildren().add(Message);
        HBox_Msg.setStyle("-fx-font-size: 15px;");
        HBox_Msg.setAlignment(Pos.BOTTOM_CENTER);


        //First Name
        Label L_FirstName= new Label("First Name:");
        grid.add(L_FirstName, 0, 0);
        TextField T_FirstName= new TextField();
        T_FirstName.setPrefHeight(40);
        grid.add(T_FirstName, 1, 0);

        //Last Name
        Label L_LastName= new Label("Last Name:");
        grid.add(L_LastName, 0, 1);
        TextField T_LastName= new TextField();
        T_LastName.setPrefHeight(40);
        grid.add(T_LastName, 1, 1);

        //Address
        Label L_Address= new Label("Address:");
        grid.add(L_Address, 0, 2);
        TextField T_Address= new TextField();
        T_Address.setPrefHeight(40);
        grid.add(T_Address, 1, 2);

        //Postal Code
        Label L_PostCode= new Label("Postal Code:");
        grid.add(L_PostCode, 0, 3);
        TextField T_PostCode= new TextField();
        T_PostCode.setPrefHeight(40);
        grid.add(T_PostCode, 1, 3);

        //Province
        Label L_Province= new Label("Province:");
        grid.add(L_Province, 0, 4);
        TextField T_Province= new TextField();
        T_Province.setPrefHeight(40);
        grid.add(T_Province, 1, 4);


        //Phone
        String RegexPhone="^\\d{10}";
        Label L_Phone= new Label("Phone Number:");
        grid.add(L_Phone, 0, 5);
        IntegerField T_Phone= new IntegerField();
        T_Phone.setPrefHeight(40);
        //giving warning if the number is not 9 digits
        T_Phone.textProperty().addListener((observable, oldValue,newValue)->{
            if(newValue.isEmpty()) {
                Message.setText("");
            }
            else if(!newValue.matches(RegexPhone)) {
                Message.setText("Warning: Phone Number must be 10 digits!");
            }else {
                Message.setText("");
            }
        });
        grid.add(T_Phone, 1, 5);

        //buttons
        HBox Hbox_buttons=new HBox(20);
        Hbox_buttons.setPadding(new Insets(20,0,0,0));
        Button B_AddPlayer=new Button("Add Player");
        Button B_Clear= new Button("Clear");
        Hbox_buttons.getChildren().addAll(B_AddPlayer ,B_Clear);


        //center buttons
        Hbox_buttons.setAlignment(Pos.BOTTOM_CENTER);



        B_AddPlayer.setOnAction(e->{
            AddPlayer(T_FirstName,
                    T_LastName,
                    T_Address,
                    T_PostCode,
                    T_Province,
                    T_Phone);


        });




        TextField[] fields= { T_FirstName,T_LastName,T_Address,T_PostCode,T_Province,T_Phone};
        B_Clear.setOnAction(e ->{
            ClearText(fields);
        });


        layout.getChildren().addAll(grid, Hbox_buttons, HBox_Msg);
        layout.setStyle("-fx-font-size: 15px;");
        return layout;


    }

    //Tab: update/remove player
    public Pane layout3() {

        VBox layout= new VBox(20);

        GridPane grid = new GridPane();
//		grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(15, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        //grid constraints
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1,col2);


        //set fixed Row Height
        RowConstraints fixedRowHeight = new RowConstraints();
        fixedRowHeight.setMinHeight(40); // Minimum height of the row

        //total of 6 row for text fields
        for (int i = 0; i < 6; i++) { // Adjust the number of rows as needed
            grid.getRowConstraints().add(fixedRowHeight);
        }


        //warning area
        HBox HBox_Msg= new HBox(30);
        Label Message= new Label();
        HBox_Msg.getChildren().add(Message);
        HBox_Msg.setStyle("-fx-font-size: 15px;");
        HBox_Msg.setAlignment(Pos.BOTTOM_CENTER);

        //Search Player ID
        HBox player_id=new HBox(10);
        player_id.setAlignment(Pos.BASELINE_CENTER);
        player_id.setPadding(new Insets(20,0,0,0));
        Label L_PLAYERID= new Label("Enter Player ID:");
        IntegerField I_playerid= new IntegerField();
        I_playerid.setPrefHeight(40);
        Button B_playerID= new Button("Search");
        Button B_DeletePlayer=new Button("Delete");
        player_id.getChildren().addAll(L_PLAYERID,I_playerid,B_playerID, B_DeletePlayer);



        //First Name
        Label L_FirstName= new Label("First Name:");
        grid.add(L_FirstName, 0, 0);
        TextField T_FirstName= new TextField();
        T_FirstName.setPrefHeight(40);
        grid.add(T_FirstName, 1, 0);

        //Last Name
        Label L_LastName= new Label("Last Name:");
        grid.add(L_LastName, 0, 1);
        TextField T_LastName= new TextField();
        T_LastName.setPrefHeight(40);
        grid.add(T_LastName, 1, 1);

        //Address
        Label L_Address= new Label("Address:");
        grid.add(L_Address, 0, 2);
        TextField T_Address= new TextField();
        T_Address.setPrefHeight(40);
        grid.add(T_Address, 1, 2);

        //Postal Code
        Label L_PostCode= new Label("Postal Code:");
        grid.add(L_PostCode, 0, 3);
        TextField T_PostCode= new TextField();
        T_PostCode.setPrefHeight(40);
        grid.add(T_PostCode, 1, 3);

        //Province
        Label L_Province= new Label("Province:");
        grid.add(L_Province, 0, 4);
        TextField T_Province= new TextField();
        T_Province.setPrefHeight(40);
        grid.add(T_Province, 1, 4);


        //Phone
        Label L_Phone= new Label("Phone Number:");
        grid.add(L_Phone, 0, 5);
        IntegerField T_Phone= new IntegerField();
        T_Phone.setPrefHeight(40);
        //giving warning if the number is not 9 digits
        String RegexPhone="^\\d{10}";
        T_Phone.textProperty().addListener((observable, oldValue,newValue)->{
            if(newValue.isEmpty()) {
                Message.setText("");
            }
            else if(!newValue.matches(RegexPhone)) {
                Message.setText("Warning: Phone Number must be 10 digits!");
            }else {
                Message.setText("");
            }
        });
        grid.add(T_Phone, 1, 5);


        //buttons
        HBox Hbox_buttons=new HBox(40);
        Button B_UpdatePlayer=new Button("Update Player");

        Button B_Clear= new Button("Clear");
        Hbox_buttons.getChildren().addAll(B_UpdatePlayer, B_Clear );

        //Center buttons
        Hbox_buttons.setAlignment(Pos.TOP_CENTER);


        B_UpdatePlayer.setOnAction(e->{

            UpdatePlayer(I_playerid,
                    T_FirstName,
                    T_LastName,
                    T_Address,
                    T_PostCode,
                    T_Province,
                    T_Phone);


        });


        B_playerID.setOnAction(e->{

            TextField[] Allfields= { T_FirstName, T_LastName, T_Address, T_PostCode, T_Province, T_Phone}; //except playerid

            ClearText(Allfields);
            String sql="select first_name, last_name,"
                    + "address, postal_code,"
                    + "province, phone_number from PEICHITSENG_player "
                    + "where player_id=? ";



            try(
                    PreparedStatement pstmt=con.prepareStatement(sql)) {

                //Check player ID
                isPlayerExist(I_playerid,con);
                pstmt.setInt(1, I_playerid.getValue());

                ResultSet rs=pstmt.executeQuery();

                //Declare variables for data
                String first_name="";
                String last_name="";
                String address="";
                String postal_code="";
                String province="";
                String phone_number="";

                while(rs.next()) {

                    first_name=rs.getString("first_name");
                    last_name=rs.getString("last_name");
                    address=rs.getString("address");
                    postal_code=rs.getString("postal_code");
                    province=rs.getString("province");
                    phone_number=rs.getString("phone_number");

                }

                T_FirstName.setText(first_name);
                T_LastName.setText(last_name);
                T_Address.setText(address);
                T_PostCode.setText(postal_code);
                T_Province.setText(province);
                T_Phone.setText(phone_number);


            }
            catch(Exception error) {
                error.printStackTrace();
                Alert err=NewError(error.getMessage());
                err.show();

            }
        });

        B_DeletePlayer.setOnAction(e->{
            TextField[] Allfields= { T_FirstName, T_LastName, T_Address, T_PostCode, T_Province, T_Phone}; //except playerid

            ClearText(Allfields);
            String sql1="delete from PEICHITSENG_player_and_game "
                    + "where player_id=? ";
            String sql2="delete from PEICHITSENG_player "
                    + "where player_id=? ";


            try{

                //Check player ID
                isPlayerExist(I_playerid,con);
                PreparedStatement pstmt=con.prepareStatement(sql1);
                pstmt.setInt(1, I_playerid.getValue());
                pstmt.executeQuery();
                pstmt=con.prepareStatement(sql2);
                pstmt.setInt(1, I_playerid.getValue());
                pstmt.executeQuery();


            }
            catch(Exception error) {
                error.printStackTrace();
                Alert err=NewError(error.getMessage());
                err.show();

            }
        });

        B_Clear.setOnAction(e->{
            TextField[] Allfields= {I_playerid, T_FirstName, T_LastName, T_Address, T_PostCode, T_Province, T_Phone};
            ClearText(Allfields);
        });


        layout.getChildren().addAll(player_id,grid,Hbox_buttons,HBox_Msg);
        layout.setStyle("-fx-font-size: 15px;");
        return layout;


    }

    public Pane layout4() {
        VBox layout= new VBox();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(40);

        //grid constraints
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1,col2);

        //set fixed Row Height
        RowConstraints fixedRowHeight = new RowConstraints();
        fixedRowHeight.setMinHeight(40); // Minimum height of the row

        //total of 6 row for text fields
        for (int i = 0; i < 4; i++) { // Adjust the number of rows as needed
            grid.getRowConstraints().add(fixedRowHeight);
        }

        //Warning area
        HBox HBox_Msg= new HBox();
        HBox_Msg.setPadding(new Insets(10,0,0,0));
        Label Message= new Label();
        HBox_Msg.getChildren().add(Message);
        HBox_Msg.setStyle("-fx-font-size: 15px;");
        HBox_Msg.setAlignment(Pos.BOTTOM_CENTER);



        //Game ID
        Label L_GAMEID= new Label("Game ID:");
        grid.add(L_GAMEID, 0, 0);
        IntegerField I_GAMEID= new IntegerField();
        I_GAMEID.setPrefHeight(40);
        grid.add(I_GAMEID, 1, 0);

        //Player ID
        Label L_PlayerID= new Label("Player ID:");
        grid.add(L_PlayerID, 0, 1);
        IntegerField I_PlayerID= new IntegerField();
        I_PlayerID.setPrefHeight(40);
        grid.add(I_PlayerID, 1, 1);

        //Date
        String regexDate="^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        Label L_Date = new Label("Playing Date (YYYY-MM-DD):");
        grid.add(L_Date, 0, 2);
        TextField T_Date =new TextField();
        T_Date.setPrefHeight(40);
        //giving warning if the date format is invalid
        T_Date.textProperty().addListener((observable, oldValue, newValue)->{
            if(newValue.isEmpty()) {
                Message.setText("");
            }
            else if(!newValue.matches(regexDate)) {
                Message.setText("Warning: Please use YYYY-MM-DD.");

            }else {
                Message.setText("");
            }
        });
        grid.add(T_Date, 1, 2);

        //Score
        Label L_Score= new Label("Score:");
        grid.add(L_Score, 0, 3);
        IntegerField I_Score= new IntegerField();
        I_Score.setPrefHeight(40);
        grid.add(I_Score, 1, 3);

        //buttons
        HBox Buttons=new HBox(20);
        Button B_AddScore=new Button("New Score");
        Button B_Clear= new Button("Clear");
        Buttons.getChildren().addAll(B_AddScore, B_Clear);
        Buttons.setPadding(new Insets(40));
        Buttons.setAlignment(Pos.BOTTOM_CENTER);



        B_AddScore.setOnAction(e ->{
            AddScore(I_GAMEID, I_PlayerID, T_Date, I_Score);


        });


        B_Clear.setOnAction(e->{
            TextField[] fields= {I_GAMEID, I_PlayerID, T_Date, I_Score};
            ClearText(fields);
        });


        layout.setStyle("-fx-font-size: 15px;");
        layout.getChildren().addAll(grid,Buttons, HBox_Msg);


        return layout;
    }


    public Pane layout5() {

        VBox layout=new VBox(20);

        //Search bar
        HBox H_Player_id =new HBox(20);
        H_Player_id.setPadding(new Insets(20,0,0,0));
        H_Player_id.setAlignment(Pos.BASELINE_CENTER);

        Label L_PlayerID=new Label("Player ID:");
        IntegerField I_PlayerID=new IntegerField();
        Button B_PlayerID=new Button("Search");
        H_Player_id.getChildren().addAll(L_PlayerID,I_PlayerID , B_PlayerID);




        layout.setStyle("-fx-font-size: 15px;");
        layout.getChildren().add(H_Player_id);

        TableView<DataModel> table=new TableView<>();
        //Column: GameTitle
        TableColumn<DataModel, String> firstColumn= new TableColumn<>("Game");
        firstColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("GameTitle"));
        firstColumn.setMinWidth(200); // Set minimum width

        //Column: Score
        TableColumn<DataModel, Integer> secondColumn=new TableColumn<>("Score");
        secondColumn.setCellValueFactory(new PropertyValueFactory<DataModel, Integer>("Score"));
        secondColumn.setMinWidth(200); // Set minimum width

        //Column: Date
        TableColumn<DataModel, String> thirdColumn=new TableColumn<>("Date");
        thirdColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("Date"));
        thirdColumn.setMinWidth(200); // Set minimum width

        table.getColumns().addAll(firstColumn, secondColumn, thirdColumn);
        layout.getChildren().add(table);
        B_PlayerID.setOnAction(e->{



            String sql="select g.game_title, pg.score, to_char(pg.playing_date,'YYYY-MM-DD') Playdate from PeiChiTseng_player_and_game pg "
                    + "join PeiChiTseng_game g using(game_id) "
                    + "where pg.player_id=? ";


            ObservableList<DataModel> data = FXCollections.observableArrayList();
            try(
                    PreparedStatement pstmt=con.prepareStatement(sql)) {
                table.setItems(null);
                isPlayerExist(I_PlayerID,con);
                pstmt.setInt(1, I_PlayerID.getValue());

                ResultSet rs=pstmt.executeQuery();


                while(rs.next()) {
                    String game=rs.getString("game_title");
                    int score=rs.getInt("score");
                    String date=rs.getString("Playdate");

                    data.add(new DataModel(game,score,date));
                }

                table.setItems(data);


            }
            catch(Exception error) {
                error.printStackTrace();
                Alert err=NewError(error.getMessage());
                err.show();
            }

        });



        return layout;
    }



    private static void initializeConnection() {
        String DBURL="jdbc:oracle:thin:@199.212.26.208:1521:SQLD";
        String username="COMP228_F24_soh_29";
        String password="password123";

        try {
            System.out.println("> Start Program ...");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("> Driver Loaded successfully.");
            //establish a connection
            con = DriverManager.getConnection(DBURL,username, password);
            System.out.println("> Database connected successfully.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }


    private static void closeConnection() {
        if(con != null) {
            try {
                con.close();
                System.out.println("Connection closed successfully");
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private void AddGame(TextField GameName) {


        TextField[] fields= {GameName};
        String sql="insert into PEICHITSENG_GAME (GAME_TITLE) values (?)";



        try(PreparedStatement pstmt=con.prepareStatement(sql)) {

            //validate Fields
            isTextFieldEmpty(fields);


            pstmt.setString(1, GameName.getText());
            pstmt.executeUpdate();

            Alert msg=NewMsg("The game is added successfully!");
            msg.show();
        }
        catch(Exception e) {

            e.printStackTrace();
            Alert err=NewError(e.getMessage());
            err.show();

        }


    }




    private void AddPlayer(TextField First_name,
                           TextField Last_name,
                           TextField address,
                           TextField postal_code,
                           TextField province,
                           IntegerField phone_number) {



        TextField[] fields= {First_name, Last_name, address, postal_code, province, phone_number};

        String Table_name="PeiChiTseng_PLAYER";
        String sql="insert into "+Table_name+"(player_id,"
                + "First_name,"
                + "last_name,"
                + "address,"
                + "postal_code,"
                + "province,"
                + "phone_number) values (?,?, ?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt=con.prepareStatement(sql)) {


            //validate fields
            isTextFieldEmpty(fields);
            String RegexPhone="^\\d{10}";
            isFormatValidate(phone_number,RegexPhone);
            String RegexProvince="^[A-Za-z]{2}$";
            isFormatValidate(province,RegexProvince);


            //Generate New ID for Player
            int id=NewPlayerID(con);


            pstmt.setInt(1, id);
            pstmt.setString(2, First_name.getText());
            pstmt.setString(3, Last_name.getText());
            pstmt.setString(4, address.getText());
            pstmt.setString(5, postal_code.getText());
            pstmt.setString(6, province.getText());
            pstmt.setString(7, phone_number.getText());

            pstmt.executeUpdate();

            Alert msg=NewMsg("Added successfully\nYour player id: "+id);
            msg.show();


        }
        catch(Exception e) {

            e.printStackTrace();

            Alert err=NewError(e.getMessage());
            err.show();

        }



    }



    private void UpdatePlayer(IntegerField Player_id,
                              TextField First_name,
                              TextField Last_name,
                              TextField address,
                              TextField postal_code,
                              TextField province,
                              TextField phone_number) {


        TextField[] fields= {Player_id,First_name, Last_name, address, postal_code, province, phone_number};
        String[] fieldsname= {"First_name","Last_name", "address","postal_code", "province","phone_number"};



        try{


            //validate fields
            isPlayerExist(Player_id,con);
            isTextFieldEmpty(fields);
            String RegexPhone="^\\d{10}";
            isFormatValidate(phone_number,RegexPhone);


            String sql="Update PEICHITSENG_player set First_name=? , Last_name=?,"
                    + "address=?, postal_code=?, province=?, "
                    + "phone_number=? where player_id=?";

            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setString(1, First_name.getText());
            pstmt.setString(2, Last_name.getText());
            pstmt.setString(3, address.getText());
            pstmt.setString(4, postal_code.getText());
            pstmt.setString(5, province.getText());
            pstmt.setString(6, phone_number.getText());
            pstmt.setDouble(7, Player_id.getValue());


            int rowUpdated=pstmt.executeUpdate();
            if(rowUpdated>0) {

                Alert msg=NewMsg("User ("+Player_id.getValue()+") information was updated successfully!");
                msg.show();
            }


        }
        catch(Exception e) {

            e.printStackTrace();
            Alert err=NewError(e.getMessage());
            err.show();

        }


    }


    private void AddScore(IntegerField GameID,
                          IntegerField PlayerID,
                          TextField Date,
                          IntegerField Score) {



        TextField[] fields= {GameID, PlayerID, Date, Score};

        String Table_name="PeiChiTseng_player_and_game";
        String sql="insert into "+Table_name+"(GAME_ID,"
                + "PLAYER_ID,"
                + "PLAYING_DATE,"
                + "Score) values (?, ?, to_date(?,'YYYY-MM-DD') , ?)";


        try(
                PreparedStatement pstmt=con.prepareStatement(sql)) {

            //validate fields
            isTextFieldEmpty(fields);
            isGameExist(GameID,con);
            isPlayerExist(PlayerID,con);
            String regexDate="^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
            isFormatValidate(Date,regexDate);

            pstmt.setInt(1, GameID.getValue());
            pstmt.setInt(2, PlayerID.getValue());
            pstmt.setString(3, Date.getText());
            pstmt.setInt(4, Score.getValue());


            pstmt.executeUpdate();
            Alert msg=NewMsg("Added sucessfully");
            msg.show();

        }
        catch(Exception e) {

            e.printStackTrace();
            Alert err=NewError(e.getMessage());
            err.show();

        }



    }


    //Generate new ID for Player
    private int NewPlayerID(Connection con) {


        try {
            PreparedStatement pstmt=con.prepareStatement("select seq_player_id.nextval from dual");
            ResultSet rs=pstmt.executeQuery();
            while(rs.next()) {
                return rs.getInt(1);
            }

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return 0;


    }


    //Validate fields method

    private static void isTextFieldEmpty  (TextField[] fields) throws Exception {


        for(TextField field: fields) {
            if(field.getText().trim().isEmpty()) {

                throw new Exception("All fields are required !!!");

            }
        }

    }



    private static void isPlayerExist (IntegerField id,
                                       Connection con) throws Exception{
        String query="select count(1) from PEICHITSENG_Player where player_id=?";


        PreparedStatement pstmt=con.prepareStatement(query);
        pstmt.setInt(1, id.getValue());
        ResultSet rs=pstmt.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            System.out.println(count);
            if(count == 0) {
                throw new Exception("No such Player");
            }
        }



    }

    private static void isGameExist (IntegerField id,
                                     Connection con) throws Exception{

        String query="select count(1) from PEICHITSENG_Game where game_id=?";


        PreparedStatement pstmt=con.prepareStatement(query);
        pstmt.setInt(1, id.getValue());
        ResultSet rs=pstmt.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);

            if(count == 0) {
                throw new Exception("No such Game");
            }
        }



    }

    private static void isFormatValidate(TextField number, String regex) throws Exception{

//		String RegexPhone="^\\d{9}";
        Pattern pattern=Pattern.compile(regex);
        if(!pattern.matcher(number.getText()).matches()) {
            throw new Exception("Format is wrong!");

        }

    }


    //Clear Fields
    public static void ClearText(TextField[] fields) {

        for(int i=0; i<fields.length ; i++) {
            fields[i].setText("");
        }
    }


    //Ineteger Field
    public  class IntegerField extends TextField{
        public IntegerField() {
            this.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    setText(oldValue);
                }
            });
        }

        public int getValue() {
            try {
                return Integer.parseInt(getText());
            } catch (NumberFormatException e) {
                return 0; // Default value if parsing fails
            }
        }

        // Set the integer value to the field
        public void setValue(int value) {
            setText(String.valueOf(value));
        }

    }

    //Pop up Windows
    private Alert NewMsg(String message) {
        Alert msg=new Alert(AlertType.INFORMATION);
        msg.setTitle("Message");
        msg.setContentText(message);
        DialogPane dialogPane = msg.getDialogPane();
        dialogPane.setStyle("-fx-font-size: 14px;");
        return msg;
    }

    private Alert NewError(String message) {
        Alert err=new Alert(AlertType.ERROR);
        err.setTitle("Error");
        err.setContentText(message);
        DialogPane dialogPane = err.getDialogPane();
        dialogPane.setStyle("-fx-font-size: 14px;");
        return err;
    }


    //Data Model
    public class DataModel {

        public final SimpleStringProperty GameTitle;
        public final SimpleIntegerProperty Score;
        public final SimpleStringProperty Date;

        public DataModel(String game, int score, String date) {
            this.GameTitle = new SimpleStringProperty(game);
            this.Score = new SimpleIntegerProperty(score);
            this.Date = new SimpleStringProperty(date);
        }

        public String getGameTitle() {
            return GameTitle.get();
        }
        public void setGameTitle(String game) {
            GameTitle.set(game);
        }

        public int getScore() {
            return Score.get();
        }
        public void setScore(int score) {
            Score.set(score);
        }

        public String getDate() {
            return Date.get();
        }
        public void setDate(String date) {
            Date.set(date);
        }
    }

    public class GameModel{
        public final SimpleIntegerProperty GameID;
        public final SimpleStringProperty GameTitle;

        public GameModel(int id, String name) {
            this.GameID=new SimpleIntegerProperty(id);
            this.GameTitle=new SimpleStringProperty(name);
        }

        public int getGameID() {
            return GameID.get();
        }

        public void setGameID(int id) {
            GameID.set(id);
        }

        public String getGameTitle() {
            return GameTitle.get();
        }
        public void setDate(String title) {
            GameTitle.set(title);
        }
    }




}