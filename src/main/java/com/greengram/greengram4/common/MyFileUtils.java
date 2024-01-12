package com.greengram.greengram4.common;

import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@Getter
public class MyFileUtils {
    private final String uploadprefixPath;//final 객체 생성 후 사용할 수 있음

    //@Value("${file.dir}")
    //private String UpLoadPreFixPath
    public MyFileUtils(@Value("${file.dir}") String uploadprefixPath){
        this.uploadprefixPath = uploadprefixPath;
    }

        //폴더만들기
     public String makeFolders(String path){//경로가 파라미터로 넘어온 후
        File folder = new File(uploadprefixPath, path);//d드라이브 주소 뒤 경로 값과 두 문자열이 합쳐진다
        folder.mkdir();//폴더를 만든다
        return folder.getAbsolutePath();//그리고 그 주소값을 리턴
        //절대 주소 abxolute
         //상대 주소
     }

     //랜덤 파일명 만들기
    //범용 고유 식별자 절대 중복되지 않은 값이 나온다 uuid
     public String getRandomFileNm(){
        return UUID.randomUUID().toString();
     }

     //확장자 얻어오기
    public String getExt(String fileNm){
        String file = StringUtils.getFilenameExtension(fileNm);

        return "."+file;
    }

    //랜덤파일명 만들기 with 확장자
    public String getRandomFileNm(String originFileNm){
       return getRandomFileNm()+getExt(originFileNm);
    }

    public String getRandomFileNm(MultipartFile mf){
        String fileNm = mf.getOriginalFilename();

        return getRandomFileNm(fileNm);
    }

}
