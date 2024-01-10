package org.server.withdraw.sercive;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.server.mapper.WithdrawChannelBankCodeMapper;
import org.server.mapper.WithdrawChannelBankDetailMapper;
import org.server.withdraw.model.WithdrawChannelBankCode;
import org.server.withdraw.model.WithdrawChannelBankDetail;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

  @Resource
  private WithdrawChannelBankCodeMapper withdrawChannelBankCodeMapper;


  @Resource
  private WithdrawChannelBankDetailMapper withdrawChannelBankDetailMapper;




  /**
   * 通過提現渠道ID獲取關聯的提現渠道銀行明細，並關聯對應的提現渠道銀行碼。
   *
   * @param withdrawBankChannelId 提現渠道ID
   * @return 包含關聯銀行碼信息的提現渠道銀行明細列表
   */
  public List<WithdrawChannelBankDetail> bankDetailJoinBankCode(String withdrawBankChannelId) {
    // 查詢提現渠道銀行明細
    List<WithdrawChannelBankDetail> bankDetails = withdrawChannelBankDetailMapper.findByWithdrawBankChannelId(
        withdrawBankChannelId);

    // 提取銀行碼ID列表
    List<String> bankCodeIds = new ArrayList<>();
    for (WithdrawChannelBankDetail withdrawChannelBankDetail : bankDetails) {
      bankCodeIds.add(withdrawChannelBankDetail.getBankCodeId());
    }

    // 獲取銀行碼ID對應的銀行碼信息，並放入Map中
    Map<String, WithdrawChannelBankCode> map = getWithdrawChannelBankCodeMap(bankCodeIds);

    // 關聯提現渠道銀行碼信息到提現渠道銀行明細
    for (WithdrawChannelBankDetail detail : bankDetails) {
      if (map.containsKey(detail.getBankCodeId())) {
        detail.setWithdrawChannelBankCode(map.get(detail.getBankCodeId()));
      }
    }

    // 返回包含關聯銀行碼信息的提現渠道銀行明細列表
    return bankDetails;
  }

  /**
   * 獲取銀行碼ID對應的提現渠道銀行碼信息，並以Map形式返回。
   *
   * @param bankCodeIds 銀行碼ID列表
   * @return 包含銀行碼ID和對應提現渠道銀行碼信息的Map
   */
  public Map<String, WithdrawChannelBankCode> getWithdrawChannelBankCodeMap(List<String> bankCodeIds) {
    Map<String, WithdrawChannelBankCode> repMap = new HashMap<>();

    // 查詢銀行碼信息
    List<WithdrawChannelBankCode> channelBankCodes = withdrawChannelBankCodeMapper.findByIBankCodeIds(bankCodeIds);

    // 構建銀行碼ID和對應提現渠道銀行碼信息的Map
    for (WithdrawChannelBankCode channelBankCode : channelBankCodes) {
      repMap.put(channelBankCode.getBankCodeId(), channelBankCode);
    }

    // 返回Map
    return repMap;
  }



}
