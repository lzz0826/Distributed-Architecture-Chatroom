package mapper;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.dao.InterpersonalDAO;
import org.server.mapper.InterpersonalMapper;

public class InterpersonalMapperTest extends BaseTest{

  @Resource
  private InterpersonalMapper interpersonalMapper;


  @Test
  public void interpersonalMapperTest(){
    InterpersonalDAO build = InterpersonalDAO
        .builder()
        .id("63543")
        .userId("")
        .blacklisted(null)
        .banChatRoom("yyyyyyyy")
        .build();

    int update = interpersonalMapper.update(build);
    System.out.println(update);
  }


  @Test
  public void selectByUserIdTest(){
    InterpersonalDAO dao = interpersonalMapper.selectByUserId("4241");
    System.out.println(dao);
  }

  @Test
  public void insertInterpersonalTest(){

    List<String> blacklist = new ArrayList<>();
    blacklist.add("blacklist1");
    blacklist.add("blacklist2");

    String blacklistJoin = String.join(",", blacklist);

    List<String> blacklisted = new ArrayList<>();
    blacklisted.add("blacklisted1");
    blacklisted.add("blacklisted2");
    String blacklistedJoin = String.join(",", blacklisted);


    List<String> banChatRoom = new ArrayList<>();
    banChatRoom.add("banChatRoom1");
    banChatRoom.add("banChatRoom2");
    String banChatRoomdJoin = String.join(",", banChatRoom);




    InterpersonalDAO dao = InterpersonalDAO
        .builder()
        .id("199999")
        .userId(null)
        .blacklist(blacklistJoin)
        .blacklisted(blacklistedJoin)
        .banChatRoom(banChatRoomdJoin)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    int i = interpersonalMapper.insertInterpersonal(dao);

    System.out.println(i);

  }

  //  List<String> extractedList = new ArrayList<>();
//  String[] items = blacklistResult.split(",");
//                for (String item : items) {
//    extractedList.add(item);
//  }

}
