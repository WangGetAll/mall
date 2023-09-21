package com.wjy.user.contoller;

import com.wjy.common.response.CommonResponse;
import com.wjy.common.response.ResponseUtil;
import com.wjy.user.pojo.OAuth2Apply;
import com.wjy.user.service.OAuth2ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OAuth2ApplyController {
    @Autowired
    private OAuth2ApplyService oAuth2ApplyService;

    @PostMapping("/apply")
    public CommonResponse oAuth2Apply(@RequestHeader String personId,
            @RequestBody OAuth2Apply oAuth2Apply) {
        oAuth2ApplyService.oAuth2Apply(personId, oAuth2Apply);
        return ResponseUtil.okResponse(oAuth2Apply);
    }
    @GetMapping("/apply")
    public CommonResponse oAuth2ApplyStatus(@RequestHeader String personId) {
        List<OAuth2Apply> oAuth2ApplyList = oAuth2ApplyService.oAuth2ApplyStatus(personId);
        return ResponseUtil.okResponse(oAuth2ApplyList);
    }

    @PutMapping("/apply")
    public CommonResponse approveOAuth2Apply(@RequestParam String appName) {
        oAuth2ApplyService.approveOAuth2Apply(appName);
        return ResponseUtil.okResponse(null);
    }
}
