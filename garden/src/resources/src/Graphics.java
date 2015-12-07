package resources.src;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Graphics {

		public final static String HYDRO_VACUUM_357x75 = "/resources/grafika/hv.png";
		public final static String INDICATOR_50x50 = "/resources/grafika/indicator.gif";
		public final static String HV_LOGO_300x300 = "/resources/grafika/logo_300x300.png";
		public final static String HV_LOGO_90x90 = "/resources/grafika/logo_90x90.png";
		public final static String SZUKAJ_FUNKCJI_TUTAJ_294x110 = "/resources/grafika/szukaj.png";
		public final static String UNDERLINE_439x81 = "/resources/grafika/underline.png";
		
		
		
		public static ImageView getGraphic(String graphic){
			return new ImageView(new Image(Graphics.class.getResourceAsStream(graphic)));
		}
}
