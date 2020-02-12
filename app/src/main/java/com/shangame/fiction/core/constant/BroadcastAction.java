package com.shangame.fiction.core.constant;

/**
 * Create by Speedy on 2018/9/7
 */
public interface BroadcastAction {

    String WECHAT_LOGION_ACTION = "wechat_login";
    String WECHAT_BIND_ACTION = "wechat_bind";
    String ADD_BOOK_TO_RACK_ACTION = "add_book_to_rack";
    String REFRESH_BOOK_RACK_ACTION = "refresh_book_to_rack";
    String PUSH_PAY_SUCCESS_ACTION = "push_pay_success";
    String PUSH_GRADE_SUCCESS_ACTION = "push_upgrade_success";
    String SHARE_TO_WECHAT_SUCCESS_ACTION = "share_to_wechat_success";
    String UPDATE_SIAN_INFO = "update_sign_info";
    String UPDATE_TASK_LIST = "update_task_list";
    String READ_RED_PACKET = "read_read_packet";
    String OFFLINE_READ_RED_PACKET = "offline_read_read_packet";
    String DELETE_BOOK_FROM_RACK = "delete_book_from_rack";
    String REFRESH_BOOK_RACK_RANK = "refresh_book_rack_rank";

    String CREATE_WORKS_COMPLETE = "create_works_complete";
    String RELEASE_CHAPTER_COMPLETE = "release_chapter_complete";

    String UPDATE_READ_PROGRESS = "update_read_progress";
    String UPDATE_LOCAL_BOOK = "update_local_book";
    String UPLOAD_WIFI_BOOK = "upload_wifi_book";

    String UPDATE_LISTEN_CHAPTER = "update_listen_chapter";
    String SWITCH_AUDIO_ACTION = "switch_audio_action";
    String TOLL_PROMPT_BOX_ACTION = "toll_prompt_box_action";
    String LISTEN_LOGIN_ACTION = "listen_login_action";
    String STOP_PLAY_ACTION = "stop_play_action";
    String START_PLAY_ACTION = "start_play_action";
    String PAUSE_PLAY_ACTION = "pause_play_action";

    String JUMP_BOY_FRAGMENT = "jump_boy_fragment";
    String JUMP_GIRL_FRAGMENT = "jump_girl_fragment";
}
