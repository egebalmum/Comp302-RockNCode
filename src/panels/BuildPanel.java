package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import domain.controllers.BuildController;
import domain.building.BuildingTracker;
import domain.building.BuildingType;
import domain.SoundManager;
import factory.ViewType;
import main.EscapeFromKoc;
import main.IAppView;
import main.IPanel;
import views.AuthView;

public class BuildPanel implements IPanel {
	private JComboBox comboBox;
	private JPanel panel;
	private JButton emptyMapButton;

	private JButton helpButton;
	private JButton nextBuildingButton;
	private JButton startRunModeButton;
	private JButton saveGameButton;

	private BuildingMap BuildingMap;
	private JPanel objectPanel;

	private JTextField buildingInfo;
	private JTextPane textPane;
	private JTextPane textPane1;
	private JTextPane textPane2;

	private BuildController buildController;
	private  SoundManager sound = new SoundManager();

	public BuildPanel(IAppView appView) {
		putPaneltoFrame(appView.getFrame());
		this.buildController = new BuildController();
		initialize();
		design();
		performed();
	}

	public BuildingMap getBuildingMap() {
		return BuildingMap;
	}

	public void performed() {
		BuildingMap.addMouseListener(new MouseAdapter() {// provides empty implementation of all
			// MouseListener`s methods, allowing us to
			// override only those which interests us
			@Override // I override only one method for presentation
			public void mousePressed(MouseEvent e) {
				int x = e.getX() - 30;
				int y = e.getY()-20 ;
				System.out.printf("x: %d  y:  %d\n",x,y);
				int b = comboBox.getSelectedIndex();
				if(BuildingMap.addToMap(x, y, b)!=null){
					sound.playSoundEffect(2);
				}
				if (BuildingMap.getObjectCount() >= BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getMinReq()) {
					textPane2.setBackground(Color.GREEN);
				}
			}

		});


		emptyMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BuildingMap.emptyMap();
				textPane2.setBackground(Color.RED);
			}
		});

	}

	protected void startRunMode() {

		int minimum = BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getMinReq();
		if (BuildingMap.getObjectCount() < minimum) {
			String str = "Please select at least " + Integer.toString(minimum) + " objects !";
			JOptionPane.showMessageDialog(null, str);

		} else {
			BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).setMap_obj(BuildingMap.getMap());
			BuildingTracker.setCurrentIndex(0);
			buildController.startRun();

		}

	}

	protected void nextBuilding() {
		int minimum = BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getMinReq();
		System.out.println("BuildingMap.getObjectCount() " + BuildingMap.getObjectCount());
		if (BuildingMap.getObjectCount() < minimum) {
			String str = "Please select at least " + Integer.toString(minimum) + " objects !";
			JOptionPane.showMessageDialog(null, str);

		} else {
			System.out.println("updateBuildingMap Current Index: "+BuildingTracker.getCurrentIndex());
			BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).setMap_obj(BuildingMap.getMap());

			BuildingMap.emptyMap();
			BuildingTracker.setCurrentIndex(1 + BuildingTracker.getCurrentIndex());
			BuildingMap.setMap(BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getMap_obj());
			String str = "At least "
					+ Integer.toString(
					BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getMinReq())
					+ " object is required!";
			textPane2.setText(str);
			buildingInfo.setText(BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getType().toString());
		}
		controlOfNextButton();
	}

	private void saveGame() {
		setBuildingLists();
		((AuthView) EscapeFromKoc.getInstance().getView(ViewType.AuthView)).getAuthController().saveGameClick(false);

	}
	public void loadGameForBuilding() {
		getBuildingMap().setMapForDB();
		controlOfNextButton();

	}

	protected void showHelp() {
		buildController.openHelpScreen();

	}

	@Override
	public void putPaneltoFrame(JFrame frame) {
		panel = new JPanel();
		frame.add(this.panel);
		panel.setVisible(false);
		panel.setBounds(0, 0, 1920, 1080);
		panel.setLayout(null);
		panel.setBorder(new LineBorder(Color.BLACK));
	}

	public void setBuildingLists() {
		BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).setMap_obj(BuildingMap.getMap());
	}

	@Override
	public void design() {

		JLabel BigLabel = new JLabel("LET'S BUILD THE GAMEE !!");
		BigLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		BigLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		BigLabel.setHorizontalAlignment(SwingConstants.CENTER);
		BigLabel.setBounds(200, 6, 300, 29);
		panel.add(BigLabel);



		buildingInfo = new JTextField();
		buildingInfo.setHorizontalAlignment(SwingConstants.CENTER);
		buildingInfo.setBounds(300, 0, 130, 26);
		BuildingMap.add(buildingInfo);

		objectPanel = new JPanel();
		objectPanel.setBackground(Color.ORANGE);
		objectPanel.setLayout(null);
		objectPanel.setBorder(new LineBorder(new Color(65, 238, 67)));
		objectPanel.setBounds(910, 70, 380, 630);
		panel.add(objectPanel);
		helpButton.setBounds(200, 300, 117, 29);
		objectPanel.add(helpButton);

		nextBuildingButton.setBounds(70, 300, 117, 29);
		objectPanel.add(nextBuildingButton);

		saveGameButton.setBounds(200, 350, 117, 29);
		objectPanel.add(saveGameButton);

		startRunModeButton.setBounds(70, 400, 117, 29);
		startRunModeButton.setVisible(false);
		objectPanel.add(startRunModeButton);

		emptyMapButton = new JButton("Empty Map");
		emptyMapButton.setBounds(70, 350, 119, 23);
		objectPanel.add(emptyMapButton);

		comboBox = new JComboBox();
		comboBox.setBounds(200, 400, 138, 22);
		comboBox.addItem("Shelves");
		comboBox.addItem("Chair");
		comboBox.addItem("Recycle Bin");
		comboBox.addItem("Table");
		objectPanel.add(comboBox);

		textPane2 = new JTextPane();
		textPane2.setEditable(false);
		textPane2.setFont(new Font("Sitka Display", Font.BOLD, 11));

		textPane2.setBounds(70, 480, 200, 90);
		objectPanel.add(textPane2);

		buildingInfo.setText(BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getType().toString());
		textPane2.setText("Please select the type of the object. \nThen, click on the map. \nMake sure to place at least " + Integer.toString(BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getMinReq()) + " objects !");
		textPane2.setBackground(Color.GREEN);

		URL resource = BuildPanel.class.getResource("/visual/shelve.png");
		ImageIcon img0 = new ImageIcon(resource);
		JLabel objLabel0 = new JLabel(img0);
		objLabel0.setBounds(80, 36, 100, 100);
		objectPanel.add(objLabel0);

		resource = BuildPanel.class.getResource("/visual/chair_200.png");
		ImageIcon img1 = new ImageIcon(resource);
		JLabel objLabel1 = new JLabel(img1);
		objLabel1.setBounds(222, 36, 100, 99);
		objectPanel.add(objLabel1);

		resource = BuildPanel.class.getResource("/visual/table_S.png");
		ImageIcon img2 = new ImageIcon(resource);
		JLabel objLabel2 = new JLabel(img2);
		objLabel2.setBounds(80, 169, 100, 100);
		objectPanel.add(objLabel2);

		resource = BuildPanel.class.getResource("/visual/bin_s.png");
		ImageIcon img3 = new ImageIcon(resource);
		JLabel objLabel3 = new JLabel(img3);
		objLabel3.setBounds(222, 169, 100, 100);
		objectPanel.add(objLabel3);

	}

	public void controlOfNextButton(){
		if (BuildingTracker.getBuildingList().get(BuildingTracker.getCurrentIndex()).getType() == BuildingType.SNA) {
			nextBuildingButton.setVisible(false);
			startRunModeButton.setVisible(true);
		}
	}

	@Override
	public void showPanel(Boolean show) {

		panel.setVisible(show);
		objectPanel.setVisible(show);
		BuildingMap.setVisible(show);
		if(show) {
			System.out.println("Now showed");
			//playMusic(1);
		}
		else{
			System.out.println("Now closed");

		}

	}

	@Override
	public void initialize() {

		BuildingMap = new BuildingMap(panel);

		helpButton = new JButton("Help");
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelp();
			}
		});

		nextBuildingButton = new JButton("Next Building");
		nextBuildingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				nextBuilding();
			}
		});

		startRunModeButton = new JButton("Start Run Mode");
		startRunModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startRunMode();
			}
		});

		saveGameButton = new JButton("Save Game");
		saveGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveGame();
			}
		});
	}

}