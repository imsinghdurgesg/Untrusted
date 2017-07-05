package singh.durgesh.com.applocker.utils;



public interface AppConstants {

    boolean ENABLE_LOGGER = true;
    // Intent
    String INTENT_SIGN_UP_TYPE = "SIGN_UP_TYPE";
    String INTENT_ANY_FRAGMENT_TITLE = "title_any_fragment_tool_bar";
    String INTENT_ANY_FRAGMENT_NAME = "any_fragment_name";
    String INTENT_AFN_UPCOMING_EVENTS = "afn_upcoming_events";
    String INTENT_AFN_MY_OPPONENTS = "afn_my_opponents";
    String INTENT_FAV_SPORT_SIGNUP = "fav_sport_signup";
    String INTENT_FAV_LOC_SIGNUP = "fav_loc_signup";

    int INDIVIDUAL = 1;
    int ORGANIZATION = 2;

    String USER = "user";
    String ORG = "organization";

    String FB = "Loginfacebook";
    String GOOGLE = "googlelogin";

    String ORG_FB = "facebooklogin";
    String ORG_GOOGLE = "googlelogin";

    String APP_PREF_NAME = "AppLocker";

    // Development
    String IMAGE_BASE="http://ec2-52-36-88-102.us-west-2.compute.amazonaws.com:8086";
    //Production
    // String IMAGE_BASE = "https://api.playin.in";
    String API_BASE = IMAGE_BASE + "/v1/";

    String GOOGLE_AUTH_BASE ="https://www.googleapis.com";

    int API_RESPONCE_OK = 0;
    int API_RESPONCE_FAIL = -1;
    int API_RESPONCE_REFRESH = -2;

    int API_RESPONCE_DEFAULT = -10;

    int API_RESPONCE_OK_STD = 200;

    int API_RESPONCE_UPDATE = 1;
    int RESULT_SELECT_PLACE_INTENT = 102;
    int RESULT_SELECTED_PLACE_INTENT = 103;

    String KEY_USER_TYPE = "key_user_type";
    String PREF_KEY_SPLASH_LAUNCH_COUNT = "splashLaunchCount";
    String KEY_PLAYIN_ID = "playinId";
    String KEY_MOBILENO = "KEY_MOBILENO";
    String KEY_FLAGID = "flagId";
    String KEY_COUNTRYCODE = "countryCode";
    String KEY_PROFILE_STATUS = "profile_status";
    String KEY_EVENT_ID = "eventId";
    String REMBERME = "RememberMe";
    String KEY_USER_NAME = "USER_NAME";
    String KEY_USER_LAST_NAME = "USER_LAST_NAME";
    String KEY_USER_EMAIL_ID = "user_email_id";
    String KEY_USER_SAVE_EMAIL_ID = "user_save_email_id";
    String KEY_USER_IMAGE = "USER_IMAGE";
    String KEY_LOGIN_TYPE = "logintype";
    int LOGIN_FB = 1;
    int LOGIN_GOOGLE =2;


    String KEY_TOKEN = "token";
    String KEY_TOKEN_EXPIRE_ON = "tokenExpiresOn";
    String KEY_DEVICE_IDENTIFIER = "deviceidentifier";
    String KEY_DEVICE_NAME = "devicename";
    String KEY_DEVICE_OS_VERSION = "deviceosversion";
    String KEY_FCM_DEVICE_TOKEN = "fcmdevicetoken";
    String KEY_FCM_NEED_REFRESH = "fcmdevicetokenrefresh";


    String KEY_SERVE_AUTH = "serverAuth";
    String KEY_PROFILE_VERIFIED = "profileverified";

    String KEY_IS_FROM_SIGNUP = "IS_FROM_SIGNUP";
    String KEY_IS_FROR_EDIT = "is_for_edit";

    String SOCIAL_INFO = "social_info";


    // Response codes
    int PLACE_PICKER_REQUEST = 1;

    enum InboxStatuses {

        RECEIVED_EVENT_REQUESTS(1),
        RECEIVED_TEAM_REQUESTS(2),
        RECEIVED_ORGANIZATION_REQUESTS(3),
        SENT_EVENT_REQUESTS(4),
        SENT_TEAM_REQUESTS(5),
        SENT_ORGANIZATION_REQUESTS(6),
        SENT_TEAM_INVITES(7),
        SENT_EVENTS_INVITES(8),
        SENT_EVENT_INVITES_FOR_TEAMS(9),
        RECEIVED_EVENTS_INVITES(10),
        RECEIVED_TEAM_INVITES(11),
        RECEIVED_EVENT_INVITES_FOR_TEAMS(12);

        private int value;

        private InboxStatuses(int status) {
            this.value = status;
        }
    }

    int PASSWORD_LENGTH = 8;
    String SELECTION_MODE = "selectionmode";
    int SPORT_SELECTION_SINGLE = 0;
    int SPORT_SELECTION_MULTIPLE = 1;

    int MAP_MODE = 2;
    int LIST_MODE = 1;

    String KEY_INVITATION = "invitation";
    String KEY_TEAM_ID = "teamId";


    int INVITE_FOR_TEAM = 0;
    int INVITE_FOR_EVENT = 1;

    // Invitation Statuses
    int STATUS_PENDING = 1;
    int STATUS_ACCEPT = 2;
    int STATUS_REJECT = 3;

    int INBOX_EVENT = 1;
    int INBOX_TEAM = 2;
    int INBOX_ORAGANIZATION = 3;
    int INBOX_MESSAGE = 4;

    String INTENT_ORG_SEARCH_TYPE = "orgType";
    int ORGANISATION_SEARCH_FOR_SIGNUP = 1;
    int ORGANISATION_SEARCH_FOR_GENERAL = 2;

    int REFRESH_RETRY_COUNT = 1;
    String KEY_REFRESH_TOKEN = "refreshToken";
    String KEY_IMAGE_REFRESHED= "imageRefreshed";


    int FCM_SYNC_REQUEST_REFRESH = 1;
    int FCM_SYNC_REQUEST_NO_REFRESH = 0;

    int FCM_SYNC_REQUEST_START = 1;
    int FCM_SYNC_COMPLETE = 2;

    int MALE = 2;
    int FEMALE = 1;
    /**
     * Sports Keys
     */
    String KEY_SPORT = "keysport";
    String KEY_SPORT_FEV = "keysport_fev";
    String KEY_SPORT_SIZE = "keysport_size";

    String KEY_SPORT_ID = "sportId";
    String KEY_SPORT_IMAGE_PATH = "imagePath";
    String KEY_SPORT_NO_OF_PAYERS = "numOfPlayers";
    String KEY_SPORT_NAME = "name";
    String KEY_SPORT_FEMOUS = "isFamousSport";

    String API_GET_ALL_SPORTS = "getallsports";
    String API_GET_FEVO_SPORTS = "getFavoriteSports";
    String API_GET_FEMOUS_SPORTS = "getFamousSports";

}