package com.eservice.sinsimiot.web;
import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.pattern.Pattern;
import com.eservice.sinsimiot.service.PatternService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* Class Description: xxx
* @author eservice
* @date 2021/10/12.
*/
@RestController
@RequestMapping("/pattern")
public class PatternController {
@Resource
private PatternService patternService;

@PostMapping("/add")
public Result add(@RequestBody @NotNull Pattern pattern) {
patternService.save(pattern);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/delete")
public Result delete(@RequestParam Integer id) {
patternService.deleteById(id);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/update")
public Result update(@RequestBody @NotNull Pattern pattern) {
patternService.update(pattern);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/detail")
public Result detail(@RequestParam @NotNull Integer id) {
Pattern pattern = patternService.findById(id);
return ResultGenerator.genSuccessResult(pattern);
}

@PostMapping("/list")
public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<Pattern> list = patternService.findAll();
PageInfo pageInfo = new PageInfo(list);
return ResultGenerator.genSuccessResult(pageInfo);
}
}
