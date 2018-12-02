package service;

public class OMDbServiceSingleton {
    private static OMDbService mOMDbService;

    public static synchronized OMDbService getService(){
        if (mOMDbService == null){
            mOMDbService = OMDbServiceFactory.getOMDbService();
        }
        return mOMDbService;
    }
}
