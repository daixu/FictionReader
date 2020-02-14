package com.shangame.fiction.net.api;

import com.shangame.fiction.net.response.*;
import com.shangame.fiction.storage.model.BookListDetailResponse;
import com.shangame.fiction.storage.model.BookListResponse;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Create by Speedy on 2018/8/3
 */
public interface ApiService {

    String userService = "api/userinfo/";
    String authorService = "api/author/";
    String fmQqService = "api/fmqq/";
    String bookInfoService = "api/bookinfo/";
    String bookShelvesService = "api/bookshelves/";
    String configureService = "api/configure/";
    String taskInfoService = "api/taskInfo/";
    String newBookInfoService = "api/newbookinfo/";
    String weChatService = "api/wechat/";
    String commentService = "api/comment/";
    String chapterService = "api/chapter/";
    String userPropService = "api/userprop/";
    String giftService = "api/gift/";
    String rechargeService = "api/recharge/";
    String searchService = "api/search/";
    String signInService = "api/signin/";
    String uploadService = "api/upload/";
    String statisService = "api/statis/";
    String agentService = "api/agent/";

    //-----------userService---start--------------
    @GET(userService + "login-user")
    Observable<HttpResult<UserInfo>> accountLogin(@Query("account") String account, @Query("loginpass") String loginpass, @Query("channel") int channel, @Query("agentid") int agentid);

    @GET(userService + "get-code")
    Observable<HttpResult<String>> sendSecurityCode(@Query("phone") String phone, @Query("channel") int channel);

    @GET(userService + "get-checkcode")
    Observable<HttpResult<String>> checkCode(@Query("phone") String phone, @Query("code") String code, @Query("channel") int channel);

    @GET(userService + "reg-user")
    Observable<HttpResult<UserInfo>> regUser(@Query("phone") String phone, @Query("smscode") String smscode, @Query("logonpass") String logonpass, @Query("regtype") int regtype, @Query("channel") int channel, @Query("agentid") int agentid);

    @GET(userService + "login-usercode")
    Observable<HttpResult<UserInfo>> phoneCodeLogin(@Query("phone") String phone, @Query("smscode") String smscode, @Query("channel") int channel, @Query("agentid") int agentid);

    @GET(userService + "set-retloginpass")
    Observable<HttpResult<UserInfo>> findPassword(@Query("account") String account, @Query("loginpass") String loginpass, @Query("smscode") String smscode, @Query("channel") int channel);

    @GET(userService + "set-loginpass")
    Observable<HttpResult<Object>> changePassword(@Query("userid") long userid, @Query("loginpass") String loginpass, @Query("smscode") String smscode, @Query("channel") int channel);

    @FormUrlEncoded
    @POST(userService + "set-userinfo")
    Observable<HttpResult<UserInfo>> modifyProfile(@FieldMap Map<String, Object> map);

    @GET(userService + "get-area")
    Observable<HttpResult<ProvinceResponse>> getProvinceList(@Query("fId") int fId);

    @GET(userService + "get-area")
    Observable<HttpResult<CityResponse>> getCityList(@Query("fId") int fId);

    @GET(userService + "get-userbookcount")
    Observable<HttpResult<GetReadStatusResponse>> getReadStatus(@Query("userid") long userid);

    @GET(userService + "get-userinfo")
    Observable<HttpResult<UserInfo>> getUserInfo(@Query("userid") long userid);

    @GET(userService + "set-readingtime")
    Observable<HttpResult<Object>> sendReadHour(@Query("userid") long userid, @Query("readingtime") long readingtime, @Query("channel") int channel, @Query("lastModifyTime") long lastModifyTime, @Query("bookid") long bookid, @Query("chapterid") long chapterid);

    @GET(userService + "set-subtime")
    Observable<HttpResult<Object>> sendAppLunchDurationTime(@Query("userid") long userid, @Query("apptime") long apptime, @Query("channel") int channel);

    @GET(userService + "get-sysmsglog")
    Observable<HttpResult<SystemMessageResponse>> getSystemMessage(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(userService + "get-sysmsglog")
    Observable<HttpResult<UpdateMessagetResponse>> getUpdateMessage(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize);

    @FormUrlEncoded
    @POST(userService + "set-feedback")
    Observable<HttpResult<Object>> feedback(@Field("userid") long userid, @Field("msg") String msg, @Field("channel") int channel);

    @GET(userService + "set-loginout")
    Observable<HttpResult<Object>> exit(@Query("userid") long userid, @Query("channel") int channel);

    @GET(userService + "get-code")
    Flowable<BaseResp> getCode(@Query("phone") String phone, @Query("channel") int channel);
    //-----------userService---end--------------


    //-----------authorService---start--------------
    @GET(authorService + "get-bookdata")
    Observable<HttpResult<BookDataBean>> getBookData(@Query("bookid") int bookid);

    @GET(authorService + "get-chapterpage")
    Observable<HttpResult<ChapterResponse>> getChapter(@Query("userid") long userid, @Query("bookid") int bookid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("drafts") int drafts);

    @FormUrlEncoded
    @POST(authorService + "set-udpbook")
    Observable<HttpResult<BookDataBean>> updateBook(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(authorService + "set-addchapter")
    Observable<HttpResult<AddChapterResponse>> addChapter(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(authorService + "set-udpauthorinfo")
    Observable<HttpResult<Object>> updateAuthorInfo(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(authorService + "set-udpsiginauthor")
    Observable<HttpResult<Object>> updateSignAuthor(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(authorService + "set-finance")
    Observable<HttpResult<Object>> setFinance(@FieldMap Map<String, Object> map);

    @GET(authorService + "del-chapter")
    Observable<HttpResult<Object>> deleteChapter(@Query("cid") int cid, @Query("bookid") int bookid, @Query("volume") int volume, @Query("userid") long userid);

    @GET(authorService + "get-financedata")
    Observable<HttpResult<FinanceDataResponse>> getFinanceData(@Query("userid") long userid);

    @GET(authorService + "get-timecfg")
    Observable<HttpResult<TimeConfigResponse>> getTimeConfig(@Query("timetype") int timetype, @Query("bookid") int bookid);

    @GET(authorService + "get-reportfrom")
    Observable<HttpResult<ReportFromResponse>> getReportFrom(@Query("timetype") int timetype, @Query("bookid") int bookid, @Query("source") int source, @Query("datetype") int datetype, @Query("years") int years, @Query("times") int times);

    // 作家平台
    @GET(authorService + "get-authorinfo")
    Observable<HttpResult<AuthorInfoResponse>> getAuthorInfo(@Query("userid") long userid);

    @POST(authorService + "set-authorinfo")
    Observable<HttpResult<Object>> setAuthorInfo(@Field("userid") long userid,
                                                 @Field("channel") long channel, @Field("penname") int penname, @Field("mobilephone") long mobilephone, @Field("card") long card,
                                                 @Field("realname") String realname, @Field("address") String address, @Field("email") String email, @Field("qq") String qq,
                                                 @Field("sex") String sex, @Field("province") String province, @Field("city") String city);

    @FormUrlEncoded
    @POST(authorService + "set-authorinfo")
    Observable<HttpResult<UserInfo>> setAuthorInfo(@FieldMap Map<String, Object> map);

    @GET(authorService + "get-bookalldata")
    Observable<HttpResult<BookAllDataResponse>> getBookAllData(@Query("userid") long userid);

    @GET(authorService + "get-bookdatapage")
    Observable<HttpResult<BookDataPageResponse>> getBookData(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("maletype") int maletype);

    @FormUrlEncoded
    @POST(authorService + "set-addbook")
    Observable<HttpResult<BookDataBean>> addBook(@FieldMap Map<String, Object> map);
    //-----------authorService---end--------------


    //-----------fmQqService---start--------------
    @GET(fmQqService + "get-albumchapter")
    Observable<HttpResult<AlbumChapterDetailResponse>> getAlbumChapterDetail(@Query("userid") long userid, @Query("albumid") int albumid, @Query("cid") int cid, @Query("channel") int channel, @Query("deviceid") String deviceid);

    @GET(fmQqService + "set-advertLog")
    Observable<HttpResult<Object>> setAdvertLog(@Query("userid") long userid, @Query("albumid") int albumid);

    @GET(fmQqService + "get-albumdetails")
    Observable<HttpResult<AlubmDetailResponse>> getAlbumDetail(@Query("userid") long userid, @Query("albumid") int albumid, @Query("channel") int channel);

    @GET(fmQqService + "set-albumbookshelves")
    Observable<HttpResult<AddToBookResponse>> addAlbumBookRack(@Query("userid") long userid, @Query("albumid") long albumid, @Query("channel") int channel);

    @GET(fmQqService + "get-albumchapterpage")
    Observable<HttpResult<AlbumChapterResponse>> getAlbumChapter(@Query("userid") long userid, @Query("albumid") int albumid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel, @Query("orderby") int orderby);

    @GET(fmQqService + "get-albumchaptefig")
    Observable<HttpResult<AlbumChapterFigResponse>> getAlbumChapterConfig(@Query("userid") long userid, @Query("albumid") long albumid, @Query("chapteid") long chapteid);

    @GET(fmQqService + "set-albumsubchapte")
    Observable<HttpResult<Object>> setAlbumSubChapter(@Query("userid") long userid, @Query("albumid") long albumid, @Query("chapteid") long chapteid, @Query("subnumber") int subnumber, @Query("autotype") int autotype, @Query("channel") int channel);

    @GET(fmQqService + "get-albumSelections")
    Observable<HttpResult<AlbumSelectionResponse>> getAlbumSelections(@Query("albumid") int albumid);

    @GET(fmQqService + "get-albumsubclassfig")
    Observable<HttpResult<BookLibraryFilterTypeResponse>> getAlbumLibraryType(@Query("userid") long userid, @Query("classid") int classid);

    @GET(fmQqService + "get-albumlistpage")
    Observable<HttpResult<AlbumDataResponse>> filterAlbumByType(@QueryMap() Map<String, Object> map);

    @GET(fmQqService + "get-albumModule")
    Observable<HttpResult<AlbumModuleResponse>> getAlbumModule(@Query("userid") long userid, @Query("channel") int channel, @Query("pagecount") int pagecount);

    @GET(fmQqService + "get-albumDataPage")
    Observable<HttpResult<AlbumDataResponse>> getAlbumData(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel, @Query("status") int status);

    @GET(fmQqService + "get-albumModulePage")
    Observable<HttpResult<AlbumDataResponse>> getAlbumModulePage(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("moduleId") int moduleId, @Query("channel") int channel);

    @GET(fmQqService + "get-albumRanking")
    Observable<HttpResult<AlbumRankingResponse>> getAlbumRank(@Query("userid") long userid, @Query("daytype") int daytype, @Query("channel") int channel);
    //-----------fmQqService---end--------------


    //-----------bookInfoService---start--------------
    @GET(bookInfoService + "get-seldatapage")
    Observable<HttpResult<SearchBookResponse>> getSearchBook(@Query("userid") long userid, @Query("platform") int platform, @Query("seltype") int seltype, @Query("keywords") String keywords, @Query("malechannel") int malechannel, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(bookInfoService + "get-rankingdata")
    Observable<HttpResult<RankResponse>> getRankList(@Query("userid") long userid, @Query("malechannel") int malechannel, @Query("daytype") int daytype, @Query("channel") int channel);

    @GET(bookInfoService + "get-bookdata")
    Observable<HttpResult<HomeStoreResponse>> getBookdata(@Query("userid") long userid, @Query("malechannel") int malechannel, @Query("platform") int platform);

    @GET(bookInfoService + "get-bookdatapage")
    Observable<HttpResult<LibFilterBookByTypeResponse>> filterBookByType(@QueryMap() Map<String, Object> map);

    @GET(bookInfoService + "get-bookdetails")
    Observable<HttpResult<BookDetailResponse>> getBookDetail(@Query("userid") long userid, @Query("bookid") long bookid, @Query("clicktype") int clicktype, @Query("channel") int channel, @Query("platform") int platform);
    //-----------bookInfoService---end--------------


    //-----------bookShelvesService---start--------------
    @GET(bookShelvesService + "set-bookshelves")
    Observable<HttpResult<AddToBookResponse>> addToBookRack(@Query("userid") long userid, @Query("bookid") long bookid, @Query("channel") int channel);

    @GET(bookShelvesService + "get-shelvesdatapage")
    Observable<HttpResult<BookRackResponse>> getFilterBook(@QueryMap() Map<String, Object> map);

    @GET(bookShelvesService + "del-bookshelves")
    Observable<HttpResult<EmptyResponse>> removeFromBookRack(@Query("userid") long userid, @Query("bookidArr") String bookidArr);

    @GET(bookShelvesService + "get-shelvesRec")
    Observable<HttpResult<RecommendBookResponse>> getRecommendBook(@Query("userid") long userid, @Query("pagesize") int pagesize, @Query("channel") int channel);

    @GET(bookShelvesService + "get-mybookmarkdata")
    Observable<HttpResult<BookMarkResponse>> getBookMarkList(@Query("userid") long userid, @Query("bookid") long bookid, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(bookShelvesService + "del-bookmark")
    Observable<HttpResult<Object>> removeBookMark(@Query("userid") long userid, @Query("bookid") long bookid, @Query("chapterid") long chapterid, @Query("pid") long pid);

    @FormUrlEncoded
    @POST(bookShelvesService + "set-bookmark")
    Observable<HttpResult<Object>> addBookMark(@FieldMap() Map<String, Object> map);

    @GET(bookShelvesService + "set-addlikeshelves")
    Observable<HttpResult<PickHobbyKindResponse>> commitPickHobbyKind(@Query("userid") long userid, @Query("classarr") String classarr, @Query("channel") int channel);

    @GET(bookShelvesService + "get-myshelvesdata")
    Observable<HttpResult<BookRackResponse>> getBookRackList(@Query("userid") long userid, @Query("malechannel") int malechannel, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel);
    //-----------bookShelvesService---end--------------


    //-----------configureService---start--------------
    @GET(configureService + "get-shelvesfig")
    Observable<HttpResult<BookRackFilterConfigResponse>> getFilterConfig(@Query("userid") long userid);

    @GET(configureService + "get-noticeinfo")
    Observable<HttpResult<NoticeInfoResponse>> getNoticeInfo(@Query("page") int page, @Query("pagesize") int pagesize, @Query("noticetype") int noticetype);

    @GET(configureService + "get-booknoticeinfo")
    Observable<HttpResult<BookNoticeInfoResponse>> getBookNoticeInfo(@Query("page") int page, @Query("pagesize") int pagesize, @Query("bookid") int bookid);

    @GET(configureService + "get-picconfig")
    Observable<HttpResult<PictureConfigResponse>> getPicConfig(@Query("userid") long userid, @Query("channel") int channel, @Query("platform") int platform, @Query("malechannel") int malechannel, @Query("pictype") int pictype);

    @GET(configureService + "get-classallfig")
    Observable<HttpResult<ClassAllFigResponse>> getClassAllFig(@Query("malechannel") int malechannel);

    @GET(configureService + "get-subclassfig")
    Observable<HttpResult<BookLibraryFilterTypeResponse>> getFilterType(@Query("userid") long userid, @Query("classid") int classid, @Query("type") int type);

    @GET(configureService + "get-superclassfig")
    Observable<HttpResult<GetBookLibraryTypeResponse>> getBookLibraryType(@Query("userid") long userid, @Query("malechannel") int malechannel, @Query("channel") int channel);

    @GET(configureService + "get-picconfig")
    Observable<HttpResult<PictureConfigResponse>> getPictureConfig(@Query("userid") long userid, @Query("channel") int channel, @Query("platform") int platform, @Query("malechannel") int malechannel, @Query("pictype") int pictype);

    @GET(configureService + "get-versionfig")
    Observable<HttpResult<VersionCheckResponse>> checkNewVersion(@Query("userid") long userid, @Query("version") long version, @Query("channel") int channel);

    @GET(configureService + "get-shareconfig")
    Observable<HttpResult<ShareResponse>> getBookShareInfo(@Query("userid") long userid, @Query("bookid") long bookid, @Query("chapterid") long chapterid, @Query("channel") int channel);

    @GET(configureService + "get-shareconfig")
    Flowable<ShareConfigResp> getShareConfig(@Query("userid") long userid, @Query("bookid") long bookid, @Query("chapterid") long chapterid, @Query("channel") int channel);

    @GET(configureService + "set-sharerewards")
    Observable<HttpResult<ShareAwardResponse>> getShareAward(@Query("userid") long userid, @Query("bookid") long bookid, @Query("chapterid") long chapterid, @Query("channel") int channel);

    //@GET("configureService + set-sharetyperewards")
    //Observable<HttpResult<GetRewardResponse>> getReward(@Query("userid") long userid, @Query("sharetype") int sharetype, @Query("channel") int channel);
    //-----------configureService---end--------------


    //-----------taskInfoService---start--------------
    @GET(taskInfoService + "set-addreadingtime")
    Observable<HttpResult<ReadTimeResponse>> sendReadTime(@Query("userid") long userid, @Query("readingtime") long readingtime, @Query("channel") int channel, @Query("type") int type);

    @GET(taskInfoService + "set-addDevicetime")
    Observable<HttpResult<ReadTimeResponse>> sendOfflineReadTime(@Query("readingtime") long readingtime, @Query("channel") int channel, @Query("type") int type);

    @GET(taskInfoService + "get-tasklist")
    Observable<HttpResult<TaskListResponse>> getTaskList(@Query("userid") long userid, @Query("channel") int channel);

    @GET(taskInfoService + "set-taskreceive")
    Observable<HttpResult<TaskAwardResponse>> getTaskAward(@Query("userid") long userid, @Query("taskid") int taskid, @Query("channel") int channel);

    @GET(taskInfoService + "set-receiveLog")
    Observable<HttpResult<TaskAwardResponse>> setReceiveLog(@Query("userid") long userid, @Query("taskLogId") int taskLogId, @Query("channel") int channel);

    @GET(taskInfoService + "get-invitelist")
    Observable<HttpResult<ShareWinRedResponse>> getShareRule(@Query("userid") long userid, @Query("channel") int channel);

    @GET(taskInfoService + "get-inviteurl")
    Observable<HttpResult<GetInviteUrlResponse>> getInviteUrl(@Query("userid") long userid, @Query("channel") int channel);

    @GET(taskInfoService + "get-inviteLogpage")
    Observable<HttpResult<InviteRecordResponse>> getInviteRecords(@Query("userid") long userid, @Query("inviteid") int inviteid, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(taskInfoService + "get-taskmoduleIdpage")
    Observable<HttpResult<TaskRecommendBookResponse>> getTaskRecommendBook(@Query("userid") long userid, @Query("moduleId") int moduleId, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel);

    @GET(taskInfoService + "get-taskLogpage")
    Observable<HttpResult<RedListResponse>> getRedList(@Query("userid") long userid, @Query("datatime") String datatime, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(taskInfoService + "get-cashconfig")
    Observable<HttpResult<CashConfigResponse>> getCashConfig(@Query("userid") long userid, @Query("channel") int channel);

    @GET(taskInfoService + "set-addAdvertLog")
    Observable<HttpResult<Object>> setAddAdvertLog(@Query("userid") long userid, @Query("bookid") long bookid, @Query("reportype") int reportype);
    //-----------taskInfoService---end--------------


    //-----------newBookInfoService---start--------------
    @GET(newBookInfoService + "get-bookmodulepage")
    Observable<HttpResult<SearchBookResponse>> loadMoreByTypeBook(@Query("userid") long userid, @Query("moduleId") int moduleId, @Query("malechannel") int malechannel, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel);

    @GET(newBookInfoService + "get-bookdatapage")
    Observable<HttpResult<SearchBookResponse>> getBookDataPage(@Query("userid") long userid, @Query("malechannel") int malechannel, @Query("status") int status, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel);

    @GET(newBookInfoService + "get-booklistpage")
    Observable<HttpResult<BookListResponse>> getBookList(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel);

    @GET(newBookInfoService + "get-bookmidlistpage")
    Observable<HttpResult<BookListDetailResponse>> getBookListDetail(@Query("userid") long userid, @Query("mid") int mid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel);

    @GET(newBookInfoService + "get-malemodule")
    Observable<HttpResult<MaleChannelResponse>> getMalemodDule(@Query("userid") long userid, @Query("pagecount") int pagecount, @Query("malechannel") int malechannel, @Query("channel") int channel);

    @GET(newBookInfoService + "get-selectmodule")
    Observable<HttpResult<ChoicenessResponse>> getChoicenessData(@Query("userid") long userid, @Query("pagecount") int pagecount, @Query("channel") int channel);

    @GET(newBookInfoService + "get-bookdatapage")
    Observable<HttpResult<OthersLookResponse>> othersLookData(@Query("userid") long userid, @Query("malechannel") int malechannel, @Query("page") int page, @Query("pagesize") int pagesize, @Query("status") int status, @Query("channel") int channel);

    @GET(newBookInfoService + "get-bookdatapage")
    Observable<HttpResult<FriendReadResponse>> getFriendRead(@Query("userid") long userid, @Query("malechannel") int malechannel, @Query("page") int page, @Query("pagesize") int pagesize, @Query("status") int status, @Query("channel") int channel);

    @GET(newBookInfoService + "get-newMediaList")
    Flowable<NewsResp> getNewMediaList(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("maleChannel") int maleChannel);
    //-----------newBookInfoService---end--------------


    //-----------weChatService---start--------------
    @GET(weChatService + "wechat-login")
    Observable<HttpResult<UserInfo>> weChatLogin(@Query("code") String code, @Query("channel") int channel, @Query("agentid") int agentid);

    @GET(weChatService + "wechat-wechatbind")
    Observable<HttpResult<BindWeChatResponse>> bindWeChat(@Query("userid") long userid, @Query("code") String code, @Query("appid") String appid, @Query("channel") int channel);

    @GET(weChatService + "set-wechatcash")
    Observable<HttpResult<WeChatCashResponse>> weChatCash(@Query("userid") long userid, @Query("cashid") int cashid, @Query("appid") String appid, @Query("channel") int channel);
    //-----------weChatService---end--------------


    //-----------commentService---start--------------
    @GET(commentService + "get-comdata")
    Observable<HttpResult<BookDetailCommentResponse>> getBookComment(@Query("userid") long userid, @Query("bookid") long bookid);

    @GET(commentService + "get-compage")
    Observable<HttpResult<BookCommentByTypeResponse>> getBookCommentByType(@Query("userid") long userid, @Query("bookid") long bookid, @Query("desctype") int desctype, @Query("page") int page, @Query("pagesize") int pagesize, @Query("platform") int platform);

    @GET(commentService + "get-comreplypage")
    Observable<HttpResult<CommentReplyResponse>> getCommentReplyList(@Query("userid") long userid, @Query("bookid") long bookid, @Query("cid") long cid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("platform") int platform);

    @FormUrlEncoded
    @POST(commentService + "set-subcomment")
    Observable<HttpResult<SendCommentResponse>> sendComment(@Field("userid") long userid, @Field("bookid") long bookid, @Field("channel") int channel, @Field("parentid") long parentid, @Field("replyuserid") long replyuserid, @Field("comment") String comment);

    @GET(commentService + "set-subpra")
    Observable<HttpResult<Object>> sendLike(@QueryMap() Map<String, Object> map);

    @GET(commentService + "get-mycompage")
    Observable<HttpResult<MyCommentResponse>> getCommentList(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("platform") int platform);
    //-----------commentService---end--------------


    //-----------chapterService---start--------------
    @GET(chapterService + "get-autorenewpage")
    Observable<HttpResult<AutoPayResponse>> getAutoPayList(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(chapterService + "set-autorene")
    Observable<HttpResult<Object>> setAutoPay(@Query("userid") long userid, @Query("bookid") long bookid, @Query("autorenew") int autorenew, @Query("channel") int channel);

    @GET(chapterService + "get-chapterpage")
    Observable<HttpResult<ChapterListResponse>> getChapterList(@Query("userid") long userid, @Query("bookid") long bookid, @Query("page") int page, @Query("pagesize") int pagesize, @Query("channel") int channel);

    @FormUrlEncoded
    @POST(chapterService + "set-chapterError")
    Observable<HttpResult<Object>> feedbackError(@Field("bookid") long bookid, @Field("chapterid") long chapterid, @Field("errortype") String errortype, @Field("remark") String remark);

    @GET(chapterService + "get-chapterdetails")
    Observable<HttpResult<ChapterDetailResponse>> getChapterDetail(@Query("userid") long userid, @Query("bookid") long bookid, @Query("cid") long cid, @Query("clicktype") int clicktype, @Query("channel") int channel);

    @GET(chapterService + "get-subchaptefig")
    Observable<HttpResult<ChapterOrderConfigResponse>> getChapterOrderConfig(@Query("userid") long userid, @Query("bookid") long bookid, @Query("chapteid") long chapteid);

    @GET(chapterService + "set-subchapte")
    Observable<HttpResult<Object>> bugChapterOrder(@Query("userid") long userid, @Query("bookid") long bookid, @Query("chapteid") long chapteid, @Query("subnumber") int subnumber, @Query("autotype") int autotype, @Query("channel") int channel);
    //-----------chapterService---end--------------


    //-----------userPropService---start--------------
    @GET(userPropService + "get-propdata")
    Observable<HttpResult<CoinSummaryResponse>> getPropData(@Query("userid") long userid);

    @GET(userPropService + "get-shelvesdatapage")
    Observable<HttpResult<CoinListResponse>> getCoinList(@Query("userid") long userid, @Query("state") int state, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(userPropService + "get-newvipdata")
    Observable<HttpResult<FreeReadResponse>> getFreeReadInfo(@Query("userid") long userid);

    @GET(userPropService + "set-newvip")
    Observable<HttpResult<UserInfo>> getFreeReadPermission(@Query("userid") long userid, @Query("channel") int channel);
    //-----------userPropService---end--------------


    //-----------giftService---start--------------
    @GET(giftService + "get-compage")
    Observable<HttpResult<PlayTourResponse>> getPlayTourList(@Query("userid") long userid, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(giftService + "get-giftcfg")
    Observable<HttpResult<GetGiftListConfigResponse>> getGiftListConfig(@Query("userid") long userid, @Query("channel") int channel);

    @GET(giftService + "set-giftreward")
    Observable<HttpResult<GiveGiftResponse>> giveGift(@Query("userid") long userid, @Query("propid") int propid, @Query("pronumber") int pronumber, @Query("bookid") long bookid, @Query("channel") int channel);

    @GET(giftService + "get-bookgiftpage")
    Observable<HttpResult<ReceivedGiftResponse>> getReceivedGiftList(@Query("bookid") long bookid, @Query("page") int page, @Query("pagesize") int pagesize);
    //-----------giftService---end--------------


    //-----------rechargeService---start--------------
    @GET(rechargeService + "get-recdatapage")
    Observable<HttpResult<PayConsumeResponse>> getConsumeHistoryList(@Query("userid") long userid, @Query("rectype") int rectype, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET(rechargeService + "get-payconfig")
    Observable<HttpResult<GetPayMenthodsResponse>> getPayMethods(@Query("channel") int channel);

    @GET(rechargeService + "get-rechargefig")
    Observable<HttpResult<GetRechargeConfigResponse>> getRechargeConfig(@Query("userid") long userid, @Query("channel") int channel);

    @GET(rechargeService + "bzf-createorder")
    Observable<HttpResult<CreatWapOrderResponse>> createWapOrder(@QueryMap() Map<String, Object> map);

    @GET(rechargeService + "get-orderinfo")
    Flowable<OrderInfoResponse> getOrderInfo(@Query("orderid") String orderid);

    //-----------rechargeService---end--------------


    //-----------searchService---start--------------
    @GET(searchService + "get-searchpage")
    Observable<HttpResult<SearchInfoResponse>> getSearchInfo(@Query("userid") long userid, @Query("platform") int platform, @Query("type") int type);

    @GET(searchService + "get-searchkeywords")
    Observable<HttpResult<SearchHintResponse>> getSearchHint(@Query("userid") long userid, @Query("keywords") String keywords, @Query("type") int type);
    //-----------searchService---end--------------


    //-----------signInService---start--------------
    @GET(signInService + "get-config")
    Observable<HttpResult<SignInInfoResponse>> getSignInInfo(@Query("userid") long userid, @Query("channel") int channel, @Query("types") int types);

    @GET(signInService + "set-usersignin")
    Observable<HttpResult<SignInResponse>> signIn(@Query("userid") long userid, @Query("channel") int channel, @Query("types") int types);
    //-----------signInService---end--------------


    //-----------uploadService---start--------------
    @Multipart
    @POST(uploadService + "set-headimg")
    Observable<HttpResult<UpLoadImageResponse>> uploadImage(@Query("userid") long userid, @Part List<MultipartBody.Part> partList);

    @Multipart
    @POST(uploadService + "set-bookCover")
    Observable<HttpResult<UpLoadImageResponse>> uploadCover(@Query("bookid") int bookid, @Part List<MultipartBody.Part> partList);
    //-----------uploadService---end--------------


    //-----------statisService---start--------------
    @GET(statisService + "set-addstatis")
    Observable<HttpResult<Object>> commitMaleChannel(@Query("malechannel") int malechannel, @Query("channel") int channel);

    @GET(statisService + "set-wifibook")
    Observable<HttpResult<Object>> setWifiBook(@Query("userid") long userid, @Query("bookname") String bookname, @Query("channel") int channel);
    //-----------statisService---end--------------


    //-----------qqService---start--------------
    @GET("api/qq/qq-login")
    Observable<HttpResult<UserInfo>> qqLogin(@Query("openid") String openid, @Query("access_token") String accessToken, @Query("channel") int channel, @Query("agentid") int agentid);
    //-----------qqService---end--------------


    //-----------vipService---start--------------
    @GET("api/vip/get-vipinfo")
    Observable<HttpResult<VipInfoResponse>> getVipInfo(@Query("userid") long userid);
    //-----------vipService---end--------------


    //-----------payUrlService---start--------------
    @GET("{payurl}")
    Observable<HttpResult<CreatWapOrderResponse>> createWapOrder2(@Path("payurl") String payurl, @QueryMap() Map<String, Object> map);
    //-----------payUrlService---end--------------

    //-----------agentService---start--------------
    @GET(agentService + "get-agentIdInfo")
    Flowable<AgentIdInfoResp> getAgentIdInfo(@Query("userid") long userid);

    @GET(agentService + "get-userListPage")
    Flowable<MemberListResp> getMemberList(@QueryMap() Map<String, Object> map);

    @GET(agentService + "set-upGrade")
    Flowable<BaseResp> setUpGrade(@Query("userid") long userid, @Query("agentId") int agentId, @Query("agentGrade") int agentGrade);

    @GET(agentService + "get-agentIdInfoPage")
    Flowable<PartnerListResp> getPartnerList(@QueryMap() Map<String, Object> map);

    @GET(agentService + "get-sumPircePage")
    Flowable<SumPriceListResp> getSumPriceList(@QueryMap() Map<String, Object> map);

    @GET(agentService + "get-balancePage")
    Flowable<WithdrawListResp> getWithdrawList(@QueryMap() Map<String, Object> map);

    @GET(agentService + "get-askBalance")
    Flowable<WithdrawResp> withdraw(@Query("agentId") int agentId, @Query("ordeid") int ordeid);

    @GET(agentService + "get-orderMoneyPage")
    Flowable<RechargeListResp> getRechargeList(@QueryMap() Map<String, Object> map);

    @GET(agentService + "get-cardList")
    Flowable<CardListResp> getCardList();

    @GET(agentService + "get-shareRec")
    Flowable<ShareRecListResp> getShareRecList(@Query("channel") int channel, @Query("pagesize") int pagesize);

    @GET(agentService + "get-agentIdDetail")
    Flowable<AgentDetailResp> getAgentIdDetail(@Query("agentId") int agentId);

    @FormUrlEncoded
    @POST(agentService + "set-agentIdUdpDetail")
    Flowable<BaseResp> setAgentIdDetail(@FieldMap() Map<String, Object> map);

    @GET(agentService + "set-agentIdBind")
    Flowable<BaseResp> bindAgentId(@Query("userid") long userid, @Query("agentId") int agentId);

    @GET(agentService + "set-agentIdBind")
    Observable<HttpResult<Object>> bindAgentId2(@Query("userid") long userid, @Query("agentId") int agentId);

    @GET(agentService + "set-QrCodeBind")
    Flowable<BaseResp> bindQrCode(@Query("userid") long userid, @Query("agentId") int agentId);

    @GET(agentService + "set-redPaper")
    Flowable<RedPaperResp> setRedPaper(@Query("userid") long userid, @Query("agentId") int agentId);
    //-----------agentService---end--------------

}
