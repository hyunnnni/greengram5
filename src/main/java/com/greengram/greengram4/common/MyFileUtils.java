package com.greengram.greengram4.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@Getter//멤버필드 때문에 넣어놓음
public class MyFileUtils {
    private final String uploadprefixPath;//final 객체 생성 후 사용할 수 있음

    //@Value("${file.dir}")
    //private String UpLoadPreFixPath
    //쿠키유틸즈와 다르게 생성자를 통해서 넣었다
    public MyFileUtils(@Value("${file.dir}") String uploadprefixPath) {
        this.uploadprefixPath = uploadprefixPath;
    }

    //폴더만들기
    public String makeFolders(String path) {//경로가 파라미터로 넘어온 후
        File folder = new File(uploadprefixPath, path);//d드라이브 주소 뒤 경로 값과 두 문자열이 합쳐진다
        folder.mkdirs();//폴더를 만든다 mkdir();을 사용하면 안됨
        return folder.getAbsolutePath();//그리고 그 주소값을 리턴
        //절대 주소 abxolute
        //상대 주소
    }

    //랜덤 파일명 만들기
    //범용 고유 식별자 절대 중복되지 않은 값이 나온다 uuid
    public String getRandomFileNm() {
        return UUID.randomUUID().toString();
    }

    //확장자 얻어오기
    public String getExt(String fileNm) {
     /*   String file = StringUtils.getFilenameExtension(fileNm);

        return "."+file;*/
        return fileNm.substring(fileNm.lastIndexOf("."));
    }

    //랜덤파일명 만들기 with 확장자
    public String getRandomFileNm(String originFileNm) {
        return getRandomFileNm() + getExt(originFileNm);
    }

    public String getRandomFileNm(MultipartFile mf) {
        String fileNm = mf.getOriginalFilename();

        return getRandomFileNm(fileNm);
    }

    //메모리에 있는 내용 >> 파일로 옮기는 메소드
    public String transferTo(MultipartFile mf, String target) {
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

    public void delFolderTrigger(String relativePath){//
        delFolder(uploadprefixPath + relativePath);
    }


    public void delFolder(String folderPath) {// 폴더 아래의 폴더 및 파일 삭제, 방금 보낸 폴더는 삭제 안 함
        File folder = new File( folderPath);//폴더 만들기
        if (folder.exists()) {//존재하는지 체크
            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.isDirectory()) {//폴더인지 체크하는 작업 만약 폴더라면 file folder로 넘어가서 재시작
                    //재귀호출? 내가 나를 실행하는 것
                    /*String absolutePath = file.getAbsolutePath();
                    String targetFolder = absolutePath.replace(uploadprefixPath,"");*/
                    delFolder(file.getAbsolutePath());//다시 시작하면서 해당 폴더를 한 번 더 열게 된다
                    // 그 안의 사진을 다 삭제한 후 마저 for문을 실행
                }else {//파일이라면 삭제
                    file.delete();//삭제 완성
                }
            }
            folder.delete();// 마지막으로 담겨있던 폴더까지 삭제 후 유저 서비스에서 다시 폴더를 생성 후 업로드 사진파일을 저장
        }
    }
}
