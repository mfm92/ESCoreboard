package bannercreator;

import nations.Participant;

public abstract class BannerCreator {
	
	int bannerWidth;
	int bannerHeight;
	
	public BannerCreator (int bannerWidth, int bannerHeight) {
		this.bannerHeight = bannerHeight;
		this.bannerWidth = bannerWidth;
	}
	
	public abstract void createBanners (Participant p, String startStatus, String startGrid);

}
