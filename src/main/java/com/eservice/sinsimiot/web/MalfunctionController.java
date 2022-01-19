package com.eservice.sinsimiot.web;

import com.eservice.sinsimiot.common.Result;
import com.eservice.sinsimiot.common.ResultGenerator;
import com.eservice.sinsimiot.model.malfunction.Malfunction;
import com.eservice.sinsimiot.service.MalfunctionService;
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
* @date 2022/01/19.
*/
@RestController
@RequestMapping("/malfunction")
public class MalfunctionController {
@Resource
private MalfunctionService malfunctionService;

@PostMapping("/add")
public Result add(@RequestBody @NotNull Malfunction malfunction) {
malfunctionService.save(malfunction);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/delete")
public Result delete(@RequestParam Integer id) {
malfunctionService.deleteById(id);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/update")
public Result update(@RequestBody @NotNull Malfunction malfunction) {
malfunctionService.update(malfunction);
return ResultGenerator.genSuccessResult();
}

@PostMapping("/detail")
public Result detail(@RequestParam @NotNull Integer id) {
Malfunction malfunction = malfunctionService.findById(id);
return ResultGenerator.genSuccessResult(malfunction);
}

@PostMapping("/list")
public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<Malfunction> list = malfunctionService.findAll();
PageInfo pageInfo = new PageInfo(list);
return ResultGenerator.genSuccessResult(pageInfo);
}
}
