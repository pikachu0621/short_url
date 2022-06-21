package com.pikachu.shorts.tool;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.google.gson.Gson;
import com.pikachu.shorts.cls.DataCls;
import com.pikachu.shorts.utils.AESCBCUtils;
import com.pikachu.shorts.utils.LoadUrlUtils;
import com.pikachu.shorts.utils.LogsUtils;

/**
 * @author Pikachu
 * @Project PK短链
 * @Package com.pikachu.shorts.tool
 * @Date 2021/8/17 ( 下午 10:03 )
 * @description
 */
public class IndexCmp {


    @SuppressLint("StaticFieldLeak")
    private static IndexCmp indexCmp, indexCmp1;
    private final Activity activity;
    private IndexInterface indexInterface;
    private LoadUrlUtils loadUrlUtils;
    private InfoInterface infoInterface;
    //private int errorRe = 3, po = 0; //

    // 主页请求实例
    public static IndexCmp getInstance(Activity activity, IndexInterface indexInterface) {
       /* synchronized (IndexCmp.class) {
            if (indexCmp == null)*/
                indexCmp = new IndexCmp(activity, indexInterface);
            return indexCmp;
        //}
    }


    // 配置实例
    public static IndexCmp getInstance(Activity activity, InfoInterface infoInterface) {

        /*if (indexCmp1 == null)*/
            indexCmp1 = new IndexCmp(activity, infoInterface);
        return indexCmp1;

    }


    public IndexCmp(Activity activity, IndexInterface indexInterface) {
        this.activity = activity;
        this.indexInterface = indexInterface;
        loadUrlUtils = new LoadUrlUtils(activity);
    }


    public IndexCmp(Activity activity, InfoInterface infoInterface) {
        this.activity = activity;
        this.infoInterface = infoInterface;
        loadUrlUtils = new LoadUrlUtils(activity);
    }


    private interface ImInterface {
        void onError(DataCls dataCls);

        void onOK(DataCls dataCls);
    }

    private void startIm(String url, String str, ImInterface imInterface) {
        if (loadUrlUtils == null)
            loadUrlUtils = new LoadUrlUtils(activity);
        loadUrlUtils.setUrl(url)
                .setType(LoadUrlUtils.Type.POST)
                .setPostStr(str)
                .setOnCall(new LoadUrlUtils.OnCall() {
                    final DataCls dataClsF = new DataCls();

                    @Override
                    public void error(Exception e) {
                        dataClsF.setMsg(e.getMessage());
                        dataClsF.setCode(e.hashCode());
                        imInterface.onError(dataClsF);
                    }

                    @Override
                    public void finish(String str) {
                        LogsUtils.showLog(str);
                        String decrypt = AESCBCUtils.decrypt(str);
                        if (decrypt == null || decrypt.equals("")) {
                            dataClsF.setCode(300);
                            dataClsF.setMsg("Decryption failed");
                            imInterface.onError(dataClsF);
                            return;
                        }
                        DataCls dataCls = dataClsF;
                        try {
                            dataCls = new Gson().fromJson(decrypt, DataCls.class);
                        } catch (Exception e) {
                            LogsUtils.showLog(decrypt);
                            dataCls.setCode(e.hashCode());
                            dataCls.setMsg("Json parsing error");
                            imInterface.onError(dataCls);
                            return;
                        }
                        LogsUtils.showLog(decrypt);
                        if (dataCls.getCode() != 200) {
                            imInterface.onError(dataCls);
                            return;
                        }
                        imInterface.onOK(dataCls);
                    }
                })
                .start();
    }


    public void startHost(String url, String nameMd5) {
        startIm(url, OTool.getLoginStr(nameMd5), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.reError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.reOk(dataCls);
            }
        });
    }


    public void startLogin(String url, String nameMd5) {
        startIm(url, OTool.getLoginStr(nameMd5), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.loginError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.loginOK(dataCls);
            }
        });
    }


    public void startShort(String url, String nameMd5, String longUrl) {
        startIm(url, OTool.getShortStr(nameMd5, longUrl), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.shortError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.shortOk(dataCls);
            }
        });
    }


    public void startOffAndOn(String url, String nameMd5, String shortUrl) {
        startIm(url, OTool.getOffAndOnStr(nameMd5, shortUrl), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.offAndOnError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.offAndOnOk(dataCls);
            }
        });
    }


    public void startLookNum(String url, String nameMd5, String shortUrl) {
        //LogsUtils.showLog( OTool.getLookNumStr(nameMd5, shortUrl));
        startIm(url, OTool.getLookNumStr(nameMd5, shortUrl), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.lookNumError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.lookNumOk(dataCls);
            }
        });
    }

    public void startLookOneShort(String url, String nameMd5, String shortUrl) {
        startIm(url, OTool.getLookOneShortStr(nameMd5, shortUrl), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.oneShortError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.oneShortOk(dataCls);
            }
        });
    }


    //type = 4  id = 0    $long_url - $short_url - $is_open - $is_open_win - $amount - $user_md5  - $time
    public void startEditShort(String url, String nameMd5, long id, String longUrl, String shortUrl, boolean isOpen, boolean isOpenWin, long amount) {
        startIm(url, OTool.getEditShortStr(nameMd5, id, longUrl, shortUrl, isOpen, isOpenWin, amount), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.editError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.editOk(dataCls);
            }
        });
    }


    //读取配置
    public void startLookInfo(String url, String nameMd5) {
        startIm(url, OTool.getLookInfoStr(nameMd5), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                infoInterface.lookInfoError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                infoInterface.lookInfoOk(dataCls);
            }
        });
    }


    // 编辑配置数据
    public void startEditInfo(String url, String nameMd5, DataCls.InfoBean infoCls) {
        startIm(url, OTool.getEditInfoStr(nameMd5, infoCls), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                infoInterface.editInfoError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                infoInterface.editInfoOk(dataCls);
            }
        });
    }


    // 读取短链列表
    public void startReadShortData(String url, String nameMd5, int page, int num) {
        startIm(url, OTool.getHistoryShortStr(nameMd5, page, num), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.loadShortError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.loadShortOk(dataCls);
            }
        });
    }

    // 删除
    public void startDeleteShort(String url, String nameMd5, String shortUrl) {
        startIm(url, OTool.getDeleteShortStr(nameMd5, shortUrl), new ImInterface() {
            @Override
            public void onError(DataCls dataCls) {
                indexInterface.deleteError(dataCls);
            }

            @Override
            public void onOK(DataCls dataCls) {
                indexInterface.deleteOk(dataCls);
            }
        });
    }



}
