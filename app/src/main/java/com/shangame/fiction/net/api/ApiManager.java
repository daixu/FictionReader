package com.shangame.fiction.net.api;

import com.shangame.fiction.net.manager.HttpManager;
import com.shangame.fiction.net.response.*;
import com.shangame.fiction.storage.model.BookListDetailResponse;
import com.shangame.fiction.storage.model.BookListResponse;
import com.shangame.fiction.storage.model.UserInfo;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Create by Speedy on 2018/8/3
 */
public class ApiManager {

    private static final Object mLock = new Object();
    private static ApiManager apiManager;
    private ApiService apiService;

    private ApiManager() {
        apiService = HttpManager.getRetrofit().create(ApiService.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            synchronized (mLock) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

    //-----------userService---start--------------

    /**
     * 登录
     *
     * @param account
     * @param loginPass
     * @return
     */
    public Observable<HttpResult<UserInfo>> accountLogin(String account, String loginPass, int agentId) {
        return apiService.accountLogin(account, loginPass, ApiConstant.Channel.ANDROID, agentId);
    }

    /**
     * 获取手机验证码
     *
     * @param phone
     * @return
     */
    public Observable<HttpResult<String>> sendSecurityCode(String phone) {
        return apiService.sendSecurityCode(phone, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<String>> checkCode(String phone, String code) {
        return apiService.checkCode(phone, code, ApiConstant.Channel.ANDROID);
    }

    /**
     * 手机注册用户
     *
     * @param phone
     * @param logonPass
     * @param smsCode
     * @param regType
     * @param channel
     * @return
     */
    public Observable<HttpResult<UserInfo>> regUser(String phone, String logonPass, String smsCode, int regType, int channel, int agentId) {
        return apiService.regUser(phone, smsCode, logonPass, regType, channel, agentId);
    }

    public Observable<HttpResult<UserInfo>> phoneCodeLogin(String phone, String smsCode, int channel, int agentId) {
        return apiService.phoneCodeLogin(phone, smsCode, channel, agentId);
    }

    public Observable<HttpResult<UserInfo>> findPassword(String account, String loginPass, String smsCode) {
        return apiService.findPassword(account, loginPass, smsCode, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<Object>> changePassword(long userId, String loginPass, String smsCode) {
        return apiService.changePassword(userId, loginPass, smsCode, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<UserInfo>> modifyProfile(Map<String, Object> map) {
        return apiService.modifyProfile(map);
    }

    public Observable<HttpResult<ProvinceResponse>> getProvinceList() {
        return apiService.getProvinceList(0);
    }

    public Observable<HttpResult<CityResponse>> getCityList(int fId) {
        return apiService.getCityList(fId);
    }

    public Observable<HttpResult<GetReadStatusResponse>> getReadStatus(long userId) {
        return apiService.getReadStatus(userId);
    }

    public Observable<HttpResult<UserInfo>> getUserInfo(long userId) {
        return apiService.getUserInfo(userId);
    }

    public Observable<HttpResult<Object>> sendReadHour(long userId, long readingTime, long lastModifyTime, long bookId, long chapterId) {
        return apiService.sendReadHour(userId, readingTime, ApiConstant.Channel.ANDROID, lastModifyTime, bookId, chapterId);
    }

    public Observable<HttpResult<Object>> sendAppLunchDurationTime(long userId, long appTime) {
        return apiService.sendAppLunchDurationTime(userId, appTime, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<SystemMessageResponse>> getSystemMessage(long userId, int page, int pageSize) {
        return apiService.getSystemMessage(userId, page, pageSize);
    }

    public Observable<HttpResult<UpdateMessagetResponse>> getUpdateMessage(long userId, int page, int pageSize) {
        return apiService.getUpdateMessage(userId, page, pageSize);
    }

    public Observable<HttpResult<Object>> feedback(long userId, String msg) {
        return apiService.feedback(userId, msg, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<Object>> exit(long userId) {
        return apiService.exit(userId, ApiConstant.Channel.ANDROID);
    }

    public Flowable<BaseResp> getCode(String phone) {
        return apiService.getCode(phone, ApiConstant.Channel.ANDROID);
    }
    //-----------userService---end--------------


    //-----------authorService---start--------------
    public Observable<HttpResult<BookDataBean>> getBookData(int bookId) {
        return apiService.getBookData(bookId);
    }

    public Observable<HttpResult<ChapterResponse>> getChapter(long userId, int bookId, int page, int pageSize, int drafts) {
        return apiService.getChapter(userId, bookId, page, pageSize, drafts);
    }

    public Observable<HttpResult<BookDataBean>> updateBook(Map<String, Object> map) {
        return apiService.updateBook(map);
    }

    public Observable<HttpResult<AddChapterResponse>> addChapter(Map<String, Object> map) {
        return apiService.addChapter(map);
    }

    public Observable<HttpResult<Object>> updateAuthorInfo(Map<String, Object> map) {
        return apiService.updateAuthorInfo(map);
    }

    public Observable<HttpResult<Object>> updateSignAuthor(Map<String, Object> map) {
        return apiService.updateSignAuthor(map);
    }

    public Observable<HttpResult<Object>> setFinance(Map<String, Object> map) {
        return apiService.setFinance(map);
    }

    public Observable<HttpResult<Object>> deleteChapter(int cid, int bookId, int volume, long userId) {
        return apiService.deleteChapter(cid, bookId, volume, userId);
    }

    public Observable<HttpResult<FinanceDataResponse>> getFinanceData(long userId) {
        return apiService.getFinanceData(userId);
    }

    public Observable<HttpResult<TimeConfigResponse>> getTimeConfig(int timeType, int bookId) {
        return apiService.getTimeConfig(timeType, bookId);
    }

    public Observable<HttpResult<ReportFromResponse>> getReportFrom(int timeType, int bookId, int source, int dateType, int years, int times) {
        return apiService.getReportFrom(timeType, bookId, source, dateType, years, times);
    }

    public Observable<HttpResult<AuthorInfoResponse>> getAuthorInfo(long userId) {
        return apiService.getAuthorInfo(userId);
    }

    public Observable<HttpResult<UserInfo>> setAuthorInfo(Map<String, Object> map) {
        return apiService.setAuthorInfo(map);
    }

    public Observable<HttpResult<BookAllDataResponse>> getBookAllData(long userId) {
        return apiService.getBookAllData(userId);
    }

    public Observable<HttpResult<BookDataPageResponse>> getBookData(long userId, int page, int pagesize, int maletype) {
        return apiService.getBookData(userId, page, pagesize, maletype);
    }

    public Observable<HttpResult<BookDataBean>> addBook(Map<String, Object> map) {
        return apiService.addBook(map);
    }
    //-----------authorService---end--------------


    //-----------fmQqService---start--------------
    public Observable<HttpResult<AlbumChapterDetailResponse>> getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId) {
        return apiService.getAlbumChapterDetail(userId, albumId, cid, ApiConstant.Channel.ANDROID, deviceId);
    }

    public Observable<HttpResult<Object>> setAdvertLog(long userId, int albumId) {
        return apiService.setAdvertLog(userId, albumId);
    }

    public Observable<HttpResult<AlubmDetailResponse>> getAlbumDetail(long userId, int albumId) {
        return apiService.getAlbumDetail(userId, albumId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<AddToBookResponse>> addAlbumBookRack(long userId, long albumId) {
        return apiService.addAlbumBookRack(userId, albumId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<AlbumChapterResponse>> getAlbumChapter(long userId, int albumId, int page, int pageSize, int orderBy) {
        return apiService.getAlbumChapter(userId, albumId, page, pageSize, ApiConstant.Channel.ANDROID, orderBy);
    }

    public Observable<HttpResult<AlbumChapterFigResponse>> getAlbumChapterConfig(long userId, long albumId, long chapterId) {
        return apiService.getAlbumChapterConfig(userId, albumId, chapterId);
    }

    public Observable<HttpResult<Object>> setAlbumSubChapter(long userId, long albumId, long chapterId, int subNumber, boolean autoPayNextChapter) {
        int autoType = (autoPayNextChapter) ? 1 : 0;
        return apiService.setAlbumSubChapter(userId, albumId, chapterId, subNumber, autoType, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<AlbumSelectionResponse>> getAlbumSelections(int albumId) {
        return apiService.getAlbumSelections(albumId);
    }

    public Observable<HttpResult<BookLibraryFilterTypeResponse>> getAlbumLibraryType(long userId, int classId) {
        return apiService.getAlbumLibraryType(userId, classId);
    }

    public Observable<HttpResult<AlbumDataResponse>> filterAlbumByType(Map<String, Object> map) {
        return apiService.filterAlbumByType(map);
    }

    public Observable<HttpResult<AlbumModuleResponse>> getAlbumModule(long userId, int pageCount) {
        return apiService.getAlbumModule(userId, ApiConstant.Channel.ANDROID, pageCount);
    }

    public Observable<HttpResult<AlbumDataResponse>> getAlbumData(long userId, int page, int pageSize, int status) {
        return apiService.getAlbumData(userId, page, pageSize, ApiConstant.Channel.ANDROID, status);
    }

    public Observable<HttpResult<AlbumDataResponse>> getAlbumModulePage(long userId, int page, int pageSize, int moduleId) {
        return apiService.getAlbumModulePage(userId, page, pageSize, moduleId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<AlbumRankingResponse>> getAlbumRank(long userId, int dayType) {
        return apiService.getAlbumRank(userId, dayType, ApiConstant.Channel.ANDROID);
    }
    //-----------fmQqService---end--------------


    //-----------bookInfoService---start--------------
    public Observable<HttpResult<SearchBookResponse>> getSearchBook(long userId, int selType, String keywords, int bookStoreChannel, int page, int pageSize) {
        return apiService.getSearchBook(userId, ApiConstant.Platform.APP, selType, keywords, bookStoreChannel, page, pageSize);
    }

    public Observable<HttpResult<RankResponse>> getRankList(long userId, int maleChannel, int dayType) {
        return apiService.getRankList(userId, maleChannel, dayType, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<LibFilterBookByTypeResponse>> filterBookByType(Map<String, Object> map) {
        return apiService.filterBookByType(map);
    }

    /**
     * 获取书本详细信息
     *
     * @param userId
     * @param bookId
     * @param clickType
     * @return
     */
    public Observable<HttpResult<BookDetailResponse>> getBookDetail(long userId, long bookId, int clickType) {
        return apiService.getBookDetail(userId, bookId, clickType, ApiConstant.Channel.ANDROID, ApiConstant.Platform.APP);
    }
    //-----------bookInfoService---end--------------


    //-----------bookShelvesService---start--------------
    public Observable<HttpResult<AddToBookResponse>> addToBookRack(long userId, long bookId) {
        return apiService.addToBookRack(userId, bookId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<BookRackResponse>> getFilterBook(Map<String, Object> map) {
        return apiService.getFilterBook(map);
    }

    public Observable<HttpResult<EmptyResponse>> removeFromBookRack(long userId, String bookIdArr) {
        return apiService.removeFromBookRack(userId, bookIdArr);
    }

    public Observable<HttpResult<RecommendBookResponse>> getRecommendBook(long userId, int pageSize) {
        return apiService.getRecommendBook(userId, pageSize, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<BookMarkResponse>> getBookMarkList(long userId, long bookId, int page, int pageSize) {
        return apiService.getBookMarkList(userId, bookId, page, pageSize);
    }

    public Observable<HttpResult<Object>> removeBookMark(long userId, long bookId, long chapterId, long pid) {
        return apiService.removeBookMark(userId, bookId, chapterId, pid);
    }

    public Observable<HttpResult<Object>> addBookMark(Map<String, Object> map) {
        return apiService.addBookMark(map);
    }

    public Observable<HttpResult<PickHobbyKindResponse>> commitPickHobbyKind(long userId, String classArr) {
        return apiService.commitPickHobbyKind(userId, classArr, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<BookRackResponse>> getBookRackList(long userId, int maleChannel, int page, int pageSize) {
        return apiService.getBookRackList(userId, maleChannel, page, pageSize, ApiConstant.Channel.ANDROID);
    }
    //-----------bookShelvesService---end--------------


    //-----------configureService---start--------------
    public Observable<HttpResult<BookRackFilterConfigResponse>> getFilterConfig(long userId) {
        return apiService.getFilterConfig(userId);
    }

    public Observable<HttpResult<NoticeInfoResponse>> getNoticeInfo(int page, int pageSize, int noticeType) {
        return apiService.getNoticeInfo(page, pageSize, noticeType);
    }

    public Observable<HttpResult<BookNoticeInfoResponse>> getBookNoticeInfo(int page, int pageSize, int bookId) {
        return apiService.getBookNoticeInfo(page, pageSize, bookId);
    }

    public Observable<HttpResult<PictureConfigResponse>> getPicConfig(long userId) {
        return apiService.getPicConfig(userId, ApiConstant.Channel.ANDROID, ApiConstant.Platform.APP, 0, 1);
    }

    public Observable<HttpResult<ClassAllFigResponse>> getClassAllFig(int maleChannel) {
        return apiService.getClassAllFig(maleChannel);
    }

    public Observable<HttpResult<BookLibraryFilterTypeResponse>> getFilterType(long userId, int classId) {
        return apiService.getFilterType(userId, classId, 1);
    }

    public Observable<HttpResult<GetBookLibraryTypeResponse>> getBookLibraryType(long userId, int maleChannel) {
        return apiService.getBookLibraryType(userId, maleChannel, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<PictureConfigResponse>> getPictureConfig(long userId, int maleChannel) {
        return apiService.getPictureConfig(userId, ApiConstant.Channel.ANDROID, ApiConstant.Platform.APP, maleChannel, 0);
    }

    public Observable<HttpResult<VersionCheckResponse>> checkNewVersion(long userId, int version) {
        return apiService.checkNewVersion(userId, version, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<ShareResponse>> getBookShareInfo(long userId, long bookId, long chapterId) {
        return apiService.getBookShareInfo(userId, bookId, chapterId, ApiConstant.Channel.ANDROID);
    }

    public Flowable<ShareConfigResp> getShareConfig(long userId, long bookId, long chapterId) {
        return apiService.getShareConfig(userId, bookId, chapterId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<ShareAwardResponse>> getShareAward(long userId, long bookId, long chapterId) {
        return apiService.getShareAward(userId, bookId, chapterId, ApiConstant.Channel.ANDROID);
    }

    //    public  Observable<HttpResult<GetRewardResponse>> getReward(long userId, int shareType){
    //        return apiService.getReward(userId,shareType,ApiConstant.Channel.ANDROID);
    //    }
    //-----------configureService---end--------------


    //-----------taskInfoService---start--------------
    public Observable<HttpResult<ReadTimeResponse>> sendReadTime(long userId, long readingTime, int type) {
        return apiService.sendReadTime(userId, readingTime, ApiConstant.Channel.ANDROID, type);
    }

    public Observable<HttpResult<ReadTimeResponse>> sendOfflineReadTime(long readingTime, int type) {
        return apiService.sendOfflineReadTime(readingTime, ApiConstant.Channel.ANDROID, type);
    }

    public Observable<HttpResult<TaskListResponse>> getTaskList(long userId) {
        return apiService.getTaskList(userId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<TaskAwardResponse>> getTaskAward(long userId, int taskId) {
        return apiService.getTaskAward(userId, taskId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<TaskAwardResponse>> setReceiveLog(long userId, int taskLogId) {
        return apiService.setReceiveLog(userId, taskLogId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<ShareWinRedResponse>> getShareRule(long userId) {
        return apiService.getShareRule(userId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<GetInviteUrlResponse>> getInviteUrl(long userId) {
        return apiService.getInviteUrl(userId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<InviteRecordResponse>> getInviteRecords(long userId, int inviteId, int page, int pageSize) {
        return apiService.getInviteRecords(userId, inviteId, page, pageSize);
    }

    public Observable<HttpResult<TaskRecommendBookResponse>> getTaskRecommendBook(long userId, int moduleId, int page, int pageSize) {
        return apiService.getTaskRecommendBook(userId, moduleId, page, pageSize, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<RedListResponse>> getRedList(long userId, String dataTime, int page, int pageSize) {
        return apiService.getRedList(userId, dataTime, page, pageSize);
    }

    public Observable<HttpResult<CashConfigResponse>> getCashConfig(long userId) {
        return apiService.getCashConfig(userId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<Object>> setAddAdvertLog(long userId, long bookId, int repOrType) {
        return apiService.setAddAdvertLog(userId, bookId, repOrType);
    }
    //-----------taskInfoService---end--------------


    //-----------newBookInfoService---start--------------
    public Observable<HttpResult<SearchBookResponse>> loadMoreByTypeBook(long userId, int moduleId, int maleChannel, int page, int pageSize) {
        return apiService.loadMoreByTypeBook(userId, moduleId, maleChannel, page, pageSize, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<SearchBookResponse>> getBookDataPage(long userId, int maleChannel, int status, int page, int pageSize) {
        return apiService.getBookDataPage(userId, maleChannel, status, page, pageSize, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<BookListResponse>> getBookList(long userId, int page, int pageSize) {
        return apiService.getBookList(userId, page, pageSize, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<BookListDetailResponse>> getBookListDetail(long userId, int mid, int page, int pageSize) {
        return apiService.getBookListDetail(userId, mid, page, pageSize, ApiConstant.Channel.ANDROID);
    }

    /**
     * 书城书籍信息
     *
     * @param userId
     * @param pageCount
     * @param maleChannel
     * @return
     */
    public Observable<HttpResult<MaleChannelResponse>> getBookData(long userId, int pageCount, int maleChannel) {
        return apiService.getMalemodDule(userId, pageCount, maleChannel, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<ChoicenessResponse>> getChoicenessData(long userId, int pageCount) {
        return apiService.getChoicenessData(userId, pageCount, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<OthersLookResponse>> othersLookData(long userId, int maleChannel, int page, int pageSize, int status) {
        return apiService.othersLookData(userId, maleChannel, page, pageSize, status, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<FriendReadResponse>> getFriendRead(long userId, int maleChannel, int page, int pageSize, int status) {
        return apiService.getFriendRead(userId, maleChannel, page, pageSize, status, ApiConstant.Channel.ANDROID);
    }

    public Flowable<NewsResp> getNewMediaList(long userId, int page, int pageSize) {
        return apiService.getNewMediaList(userId, page, pageSize);
    }
    //-----------newBookInfoService---end--------------


    //-----------weChatService---start--------------
    public Observable<HttpResult<UserInfo>> weChatLogin(String code, int agentId) {
        return apiService.weChatLogin(code, ApiConstant.Channel.ANDROID, agentId);
    }

    public Observable<HttpResult<BindWeChatResponse>> bindWeChat(long userId, String code, String appId) {
        return apiService.bindWeChat(userId, code, appId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<WeChatCashResponse>> weChatCash(long userId, int cashId, String appId) {
        return apiService.weChatCash(userId, cashId, appId, ApiConstant.Channel.ANDROID);
    }
    //-----------weChatService---end--------------


    //-----------commentService---start--------------

    /**
     * 获取书本详细中的评论
     *
     * @param userId
     * @param bookId
     * @return
     */
    public Observable<HttpResult<BookDetailCommentResponse>> getBookComment(long userId, long bookId) {
        return apiService.getBookComment(userId, bookId);
    }

    /**
     * 根据类型获取评论
     *
     * @param userId
     * @param bookId
     * @param descType
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<HttpResult<BookCommentByTypeResponse>> getBookCommentByType(long userId, long bookId, int descType, int page, int pageSize) {
        return apiService.getBookCommentByType(userId, bookId, descType, page, pageSize, ApiConstant.Platform.APP);
    }

    /**
     * 获取评论回复
     *
     * @param userId
     * @param bookId
     * @param cid
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<HttpResult<CommentReplyResponse>> getCommentReplyList(long userId, long bookId, long cid, int page, int pageSize) {
        return apiService.getCommentReplyList(userId, bookId, cid, page, pageSize, ApiConstant.Platform.APP);
    }

    /**
     * 发送评论
     *
     * @param userId
     * @param bookId
     * @param parentId
     * @param replyUserId
     * @param comment
     * @return
     */
    public Observable<HttpResult<SendCommentResponse>> sendComment(long userId, long bookId, long parentId, long replyUserId, String comment) {
        return apiService.sendComment(userId, bookId, ApiConstant.Channel.ANDROID, parentId, replyUserId, comment);
    }

    public Observable<HttpResult<Object>> sendLike(Map<String, Object> map) {
        return apiService.sendLike(map);
    }

    public Observable<HttpResult<MyCommentResponse>> getCommentList(long userId, int page, int pageSize) {
        return apiService.getCommentList(userId, page, pageSize, ApiConstant.Platform.APP);
    }
    //-----------commentService---end--------------


    //-----------chapterService---start--------------
    public Observable<HttpResult<AutoPayResponse>> getAutoPayList(long userId, int page, int pageSize) {
        return apiService.getAutoPayList(userId, page, pageSize);
    }

    public Observable<HttpResult<Object>> setAutoPay(long userId, long bookId, int autoRenew) {
        return apiService.setAutoPay(userId, bookId, autoRenew, ApiConstant.Channel.ANDROID);
    }

    /**
     * 获取章节目录
     *
     * @param userId
     * @param bookId
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<HttpResult<ChapterListResponse>> getChapterList(long userId, long bookId, int page, int pageSize) {
        return apiService.getChapterList(userId, bookId, page, pageSize, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<Object>> feedbackError(long bookId, long chapterId, String errorType, String remark) {
        return apiService.feedbackError(bookId, chapterId, errorType, remark);
    }

    /**
     * 获取章节内容
     *
     * @param userId
     * @param bookId
     * @param cid
     * @return
     */
    public Observable<HttpResult<ChapterDetailResponse>> getChapterDetail(long userId, long bookId, long cid) {
        return apiService.getChapterDetail(userId, bookId, cid, ApiConstant.ClickType.FROM_CLICK, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<ChapterOrderConfigResponse>> getChapterOrderConfig(long userId, long bookId, long chapterId) {
        return apiService.getChapterOrderConfig(userId, bookId, chapterId);
    }

    public Observable<HttpResult<Object>> bugChapterOrder(long userId, long bookId, long chapterId, int subNumber, boolean autoPayNextChapter) {
        int autoType = (autoPayNextChapter) ? 1 : 0;
        return apiService.bugChapterOrder(userId, bookId, chapterId, subNumber, autoType, ApiConstant.Channel.ANDROID);
    }
    //-----------chapterService---end--------------


    //-----------userPropService---start--------------
    public Observable<HttpResult<CoinSummaryResponse>> getPropData(long userId) {
        return apiService.getPropData(userId);
    }

    public Observable<HttpResult<CoinListResponse>> getCoinList(long userId, int state, int page, int pageSize) {
        return apiService.getCoinList(userId, state, page, pageSize);
    }

    public Observable<HttpResult<FreeReadResponse>> getFreeReadInfo(long userId) {
        return apiService.getFreeReadInfo(userId);
    }

    public Observable<HttpResult<UserInfo>> getFreeReadPermission(long userId) {
        return apiService.getFreeReadPermission(userId, ApiConstant.Channel.ANDROID);
    }
    //-----------userPropService---end--------------


    //-----------giftService---start--------------
    public Observable<HttpResult<PlayTourResponse>> getPlayTourList(long userId, int page, int pageSize) {
        return apiService.getPlayTourList(userId, page, pageSize);
    }

    public Observable<HttpResult<GetGiftListConfigResponse>> getGiftListConfig(long userId) {
        return apiService.getGiftListConfig(userId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<GiveGiftResponse>> giveGift(long userId, int propId, int proNumber, long bookId) {
        return apiService.giveGift(userId, propId, proNumber, bookId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<ReceivedGiftResponse>> getReceivedGiftList(long bookId, int page, int pageSize) {
        return apiService.getReceivedGiftList(bookId, page, pageSize);
    }
    //-----------giftService---end--------------


    //-----------rechargeService---start--------------
    public Observable<HttpResult<PayConsumeResponse>> getConsumeHistoryList(long userId, int type, int page, int pageSize) {
        return apiService.getConsumeHistoryList(userId, type, page, pageSize);
    }

    public Observable<HttpResult<GetPayMenthodsResponse>> getPayMethods() {
        return apiService.getPayMethods(ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<GetRechargeConfigResponse>> getRechargeConfig(long userId) {
        return apiService.getRechargeConfig(userId, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<CreatWapOrderResponse>> createWapOrder(Map<String, Object> map) {
        return apiService.createWapOrder(map);
    }

    public Flowable<OrderInfoResponse> getOrderInfo(String orderId) {
        return apiService.getOrderInfo(orderId);
    }
    //-----------rechargeService---end--------------


    //-----------searchService---start--------------
    public Observable<HttpResult<SearchInfoResponse>> getSearchInfo(long userId) {
        return apiService.getSearchInfo(userId, ApiConstant.Platform.APP, 1);
    }

    public Observable<HttpResult<SearchHintResponse>> getSearchHint(long userId, String keywords) {
        return apiService.getSearchHint(userId, keywords, 1);
    }
    //-----------searchService---end--------------


    //-----------signInService---start--------------
    public Observable<HttpResult<SignInInfoResponse>> getSignInInfo(long userId) {
        return apiService.getSignInInfo(userId, ApiConstant.Channel.ANDROID, 1);
    }

    public Observable<HttpResult<SignInResponse>> signIn(long userId) {
        return apiService.signIn(userId, ApiConstant.Channel.ANDROID, 1);
    }
    //-----------signInService---end--------------


    //-----------uploadService---start--------------
    public Observable<HttpResult<UpLoadImageResponse>> uploadImage(long userId, String filePath) {
        File file = new File(filePath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart(userId + ".webp", file.getName(), imageBody);

        List<MultipartBody.Part> parts = builder.build().parts();
        return apiService.uploadImage(userId, parts);
    }

    public Observable<HttpResult<UpLoadImageResponse>> uploadCover(int bookId, String filePath) {
        File file = new File(filePath);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart(bookId + ".webp", file.getName(), imageBody);

        List<MultipartBody.Part> parts = builder.build().parts();
        return apiService.uploadCover(bookId, parts);
    }
    //-----------uploadService---end--------------


    //-----------statisService---start--------------
    public Observable<HttpResult<Object>> commitMaleChannel(int commitMaleChannel) {
        return apiService.commitMaleChannel(commitMaleChannel, ApiConstant.Channel.ANDROID);
    }

    public Observable<HttpResult<Object>> setWifiBook(long userId, String bookName) {
        return apiService.setWifiBook(userId, bookName, ApiConstant.Channel.ANDROID);
    }
    //-----------statisService---end--------------


    //-----------qqService---start--------------
    public Observable<HttpResult<UserInfo>> qqLogin(String openId, String accessToken, int agentId) {
        return apiService.qqLogin(openId, accessToken, ApiConstant.Channel.ANDROID, agentId);
    }
    //-----------qqService---end--------------


    //-----------vipService---start--------------
    public Observable<HttpResult<VipInfoResponse>> getVipInfo(long userId) {
        return apiService.getVipInfo(userId);
    }
    //-----------vipService---end--------------


    //-----------payUrlService---start--------------
    public Observable<HttpResult<CreatWapOrderResponse>> createWapOrder2(String payUrl, Map<String, Object> map) {
        return apiService.createWapOrder2(payUrl, map);
    }
    //-----------payUrlService---end--------------

    //-----------agentService---start--------------
    public Flowable<AgentIdInfoResp> getAgentIdInfo(long userId) {
        return apiService.getAgentIdInfo(userId);
    }

    public Flowable<MemberListResp> getMemberList(Map<String, Object> map) {
        return apiService.getMemberList(map);
    }

    public Flowable<BaseResp> setUpGrade(long userId, int agentId, int agentGrade) {
        return apiService.setUpGrade(userId, agentId, agentGrade);
    }

    public Flowable<PartnerListResp> getPartnerList(Map<String, Object> map) {
        return apiService.getPartnerList(map);
    }

    public Flowable<SumPriceListResp> getSumPriceList(Map<String, Object> map) {
        return apiService.getSumPriceList(map);
    }

    public Flowable<WithdrawListResp> getWithdrawList(Map<String, Object> map) {
        return apiService.getWithdrawList(map);
    }

    public Flowable<WithdrawResp> withdraw(int agentId, int orderId) {
        return apiService.withdraw(agentId, orderId);
    }

    public Flowable<RechargeListResp> getRechargeList(Map<String, Object> map) {
        return apiService.getRechargeList(map);
    }

    public Flowable<CardListResp> getCardList() {
        return apiService.getCardList();
    }

    public Flowable<ShareRecListResp> getShareRecList(int pageSize) {
        return apiService.getShareRecList(ApiConstant.Channel.ANDROID, pageSize);
    }

    public Flowable<AgentDetailResp> getAgentIdDetail(int agentId) {
        return apiService.getAgentIdDetail(agentId);
    }

    public Flowable<BaseResp> setAgentIdDetail(Map<String, Object> map) {
        return apiService.setAgentIdDetail(map);
    }

    public Flowable<BaseResp> bindAgentId(long userId, int agentId) {
        return apiService.bindAgentId(userId, agentId);
    }

    public Observable<HttpResult<Object>> bindAgentId2(long userId, int agentId) {
        return apiService.bindAgentId2(userId, agentId);
    }

    public Flowable<BaseResp> bindQrCode(long userId, int agentId) {
        return apiService.bindQrCode(userId, agentId);
    }

    public Flowable<RedPaperResp> setRedPaper(long userId, int agentId) {
        return apiService.setRedPaper(userId, agentId);
    }
    //-----------agentService---end--------------
}
