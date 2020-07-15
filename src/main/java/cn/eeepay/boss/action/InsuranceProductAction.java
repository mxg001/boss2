package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InsuranceProduct;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.InsuranceProductService;
import cn.eeepay.framework.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 保险产品管理
 * @author xieh
 * @date 2018/7/13
 */
@Controller
@RequestMapping("/insuranceProduct")
public class InsuranceProductAction {

    private Logger log = LoggerFactory.getLogger(InsuranceProductAction.class);

    @Resource
    private InsuranceProductService insuranceProductService;
    

    /**
     * 保险产品管理
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Result selectList(@RequestBody InsuranceProduct baseInfo,
                             @RequestParam(defaultValue = "1")int pageNo,
                             @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            Page<InsuranceProduct> page = new Page<>(pageNo, pageSize);
            insuranceProductService.selectList(baseInfo, page);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(page);
        } catch (Exception e){
            log.error("保险产品管理查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 保险产品详情
     * @param id
     * @return
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Result productDetail(@PathVariable("id")Long id){
        Result result = new Result();
        try {
            if(id == null){
                result.setMsg("参数不能为空");
                return result;
            }
            InsuranceProduct baseInfo = insuranceProductService.selectDetail(id);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(baseInfo);
        } catch (Exception e){
            log.error("保险产品详情查询异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 新增保险产品
     * @return
     */
    @RequestMapping("/addProduct")
    @ResponseBody
    @SystemLog(description = "新增保险产品", operCode = "insuranceProduct.addBank")
    public Result addProduct(@RequestBody InsuranceProduct info){
        Result result = new Result();
        try {
            if(info.getCompanyNo()==null){
                result.setMsg("请选择公司别称");
                return result;
            }
            boolean b = insuranceProductService.selectOrderExists(info.getShowOrder());
            if(b){
                result.setMsg("显示顺序已存在");
                return result;
            }
             b = insuranceProductService.selectProductIdExists(info.getUpperProductId());
            if(b){
                result.setMsg("上游产品id已存在");
                return result;
            }
            b=insuranceProductService.selectProductNameExists(info.getProductName());
            if(b){
                result.setMsg("产品名已经存在");
                return result;
            }
            int num = insuranceProductService.addProduct(info);
            if (num > 0){
        			 result.setStatus(true);
        			 result.setMsg("操作成功");
            }
        } catch (Exception e){
            log.error("新增保险产品", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 删除保险产品
     * @return
     */
    @RequestMapping("/deleteProduct")
    @ResponseBody
    @SystemLog(description = "删除保险产品", operCode = "insuranceProduct.deleteBank")
    public Result deleteProduct(@RequestBody InsuranceProduct info){
        Result result = new Result();
        try {
                if(info!=null) {
                    if(info.getStatus()==0){
                        insuranceProductService.deleteProduct(info);
                        result.setStatus(true);
                        result.setMsg("操作成功");
                        return result;
                    }
                    result.setMsg("当前状态为上架不能删除");
                }else {
                    result.setMsg("参数不能为空");
                }

        } catch (Exception e){
            log.error("删除保险产品异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }



    /**
     * 修改保险产品
     * @return
     */
    @RequestMapping("/updateProduct")
    @ResponseBody
    @SystemLog(description = "修改保险产品", operCode = "insuranceProduct.updateProduct")
    public Result updateBank(@RequestBody InsuranceProduct info){
        Result result = new Result();
        try {
            boolean b = insuranceProductService.selectOrderIdExists(info.getShowOrder(),info.getProductId().longValue());
            if(b){
                result.setMsg("显示顺序已存在");
                return result;
            }
            b = insuranceProductService.selectProductIdExists(info.getUpperProductId(),info.getProductId().longValue());
            if(b){
                result.setMsg("上游产品id已存在");
                return result;
            }
            b=insuranceProductService.selectProductNameIdExists(info.getProductName(),info.getProductId().longValue());
            if(b){
                result.setMsg("产品名已存在");
                return result;
            }
            int num = insuranceProductService.updateProduct(info);
            if (num == 1) {
                result.setStatus(true);
                result.setMsg("操作成功");
            }
        } catch (Exception e){
            log.error("修改保险产品异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 修改修改保险产品上架状态
     * @param info
     * @return
     */
    @RequestMapping("/updateProductStatus")
    @ResponseBody
    @SystemLog(description = "修改保险产品上架状态", operCode = "insuranceProduct.updateProductStatus")
	public Result updateBankStatus(@RequestBody InsuranceProduct info) {
		Result result = new Result();
		try {
			int num = insuranceProductService.updateProductStatus(info);
			if (num == 1) {
						result.setStatus(true);
						result.setMsg("操作成功");
			}
		} catch (Exception e) {
			log.error("修改修改保险产品上架状态常", e);
			result = ResponseUtil.buildResult(e);
		}
		return result;
	}

    /**
     * 查询所有产品信息
     * @return
     */
    @RequestMapping("/getList")
    @ResponseBody
    @SystemLog(description = "查询所有产品信息", operCode = "insuranceProduct.getList")
    public Result getList() {
        Result result = new Result();
        try {
            List<InsuranceProduct> list= insuranceProductService.getListAll();
                result.setData(list);
                result.setStatus(true);
                result.setMsg("操作成功");
        } catch (Exception e) {
            log.error("查询所有产品信息异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }


}
