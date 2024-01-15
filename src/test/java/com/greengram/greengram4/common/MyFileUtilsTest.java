package com.greengram.greengram4.common;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import({MyFileUtils.class})
@TestPropertySource(properties = {
        "file.dir=D:/home/download",
})//yaml에서 가져오는 값이 아니고 임시로 만든 값을 테스트 시에만 myFileUtils에 넣는다
public class MyFileUtilsTest {
    @Autowired
    private MyFileUtils myFileUtils;

    @Test
    public void makeFolderTest(){
        String path = "/lll";
        File preFolder = new File(myFileUtils.getUploadprefixPath(),path);
        //디렉토리, 파일 둘 다 컨트롤 할 수 있음, ( 파라미터 : 접근할 수 있는 경로 )
        //두 문자열이 합쳐지게 된다

        assertEquals(false, preFolder.exists());//ggg라는 파일인지 폴더인지 존재하는 지 확인
        //지금은 존재하지 않아야 함

        String newPath = myFileUtils.makeFolders(path);
        File newFolder = new File(newPath);
        assertEquals(preFolder.getAbsolutePath(), newFolder.getAbsolutePath());
        assertEquals(true, newFolder.exists());
    }

    @Test
    public void getRandomFileNmTest(){
        String fileNm = myFileUtils.getRandomFileNm();
        System.out.println("fileNm : " + fileNm);
        assertNotNull(fileNm);
        assertNotEquals("", fileNm);
    }

    @Test
    public void getExtTest(){
        String fileNm = "abc.efg.eee.jpg";
        String ext = myFileUtils.getExt(fileNm);
        assertEquals(".jpg",ext);

        String fileNm2 = "jjj-asdfd.png";
        String ext2 = myFileUtils.getExt(fileNm2);
        assertEquals(".png",ext2);

    }

    @Test
    public void getRandomFileNm2(){

        String fileNm1 = "반갑다.친구야.jpg";
        String rFileNm1= myFileUtils.getRandomFileNm(fileNm1);
        System.out.println("rFileNm1 : "+ rFileNm1);
        //랜덤 문자열.jpeg

        String fileNm2 = "dhk.와가갖가ㅓ.pp";
        String rFileNm2= myFileUtils.getRandomFileNm(fileNm2);
        System.out.println("rFileNm2 : "+ rFileNm2);
        //랜덤문자열.pp

    }

   @Test
    public void transferToTest() throws Exception{
        String fileNm = "1기.jpg";
        String filePath = "C:/Users/Administrator/Desktop/2차 프로젝트/프로젝트 이미지 자료/짱구 뉴스/"+fileNm;
        FileInputStream fis = new FileInputStream(filePath);
        MultipartFile mf = new MockMultipartFile("pic", fileNm, "jpg", fis );

        String saveFileNm = myFileUtils.transferTo(mf, "/feed/10");
        System.out.println("saveFileNm"+ saveFileNm);
    }

}
