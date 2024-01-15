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
        folder.mkdirs();//폴더를 만든다 mkdir();을 사용하면 안됨
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
     /*   String file = StringUtils.getFilenameExtension(fileNm);

        return "."+file;*/
        return fileNm.substring(fileNm.lastIndexOf("."));
    }

    //랜덤파일명 만들기 with 확장자
    public String getRandomFileNm(String originFileNm){
       return getRandomFileNm()+getExt(originFileNm);
    }

    public String getRandomFileNm(MultipartFile mf){
        String fileNm = mf.getOriginalFilename();

        return getRandomFileNm(fileNm);
    }

    //메모리에 있는 내용 >> 파일로 옮기는 메소드
    public String transferTo(MultipartFile mf, String target){
        String fileNm = getRandomFileNm(mf);
        String folderPath = makeFolders(target);

        File saveFile = new File(folderPath, fileNm);//파일 객체를 만들어줌
        saveFile.exists();//파일이 무조건 존재하진 않는다

        try {
            mf.transferTo(saveFile);//램에 파일 내용이 메모리에 있는데 그것을 실제 옮겨줌
            return fileNm;//데이터 베이스 저장 시 필요한 랜덤한 파일명을 리턴
            //경로까지 저장하기 원할 경우에는
            // 절대경로 >> saveFile.getAbsolutePath() D드라이브부터 쫙 나옴
            // 상대경로 >> 시작점부터의 경로를 표시
            //백엔드에서 파일을 리턴할 땐 상대경로를 사용해주는 것이 더 좋다.
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
