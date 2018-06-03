package com.rw.common.utils.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.rw.common.utils.annotation.ExcelAttribute;

/**
 * @Author MR. Li
 * @Description:批量导出名单下载 vo
 * @Date: Created in 12:40 2018/5/30
 * @Modified By:
 */

public class BatchExport implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer userId;
	
	private Integer loanId;

	/**
     * 申请人姓名
     **/
    @ExcelAttribute(name = "申请人姓名", column = "A")
    private String realName;

    /**
     * 身份证号码
     **/
    @ExcelAttribute(name = "身份证号码", column = "B")
    private String idCard;

    /**
     * 手机号码
     **/
    @ExcelAttribute(name = "手机号码", column = "C")
    private String mobile;

    /**
     * 新农保账号
     **/
    @ExcelAttribute(name = "新农保账号", column = "D")
    private String insuranceNumber;

    /**
     * 银行卡
     */
    @ExcelAttribute(name = "银行卡", column = "E")
    private String bankCard;


    /**
     * 配偶姓名
     **/
    @ExcelAttribute(name = "配偶姓名", column = "F")
    private String spouseName;


    /**
     * 配偶身份证
     **/
    @ExcelAttribute(name = "配偶身份证", column = "G")
    private String spouseIdCard;


    /**
     * 配偶手机
     **/
    @ExcelAttribute(name = "配偶手机", column = "H")
    private String spouseMobile;


    /**
     * 申请贷款金额
     **/
    @ExcelAttribute(name = "申请贷款金额", column = "I")
    private BigDecimal loanAmount;

    /**
     * 资金用途
     **/
    @ExcelAttribute(name = "资金用途", column = "J")
    private String loanUse;

    private Integer loanCycleType;
    
    @ExcelAttribute(name = "贷款周期类型(月/日)", column = "K")
    private String loanCycleTypeText;

    /**
     * 贷款周期
     **/
    @ExcelAttribute(name = "贷款周期", column = "L")
    private String loanCycle;


    /**
     * 链接Id
     **/
    @ExcelAttribute(name = "链接Id", column = "M")
    private String linkId;

    /**
     * 申请时间
     **/
    @ExcelAttribute(name = "申请时间", column = "N")
    private Date applyTime;

    /**
     * 综合评分
     */
    @ExcelAttribute(name = "综合评分", column = "O")
    private Integer totalScores;

    /**
     * 大数据评分
     */
    @ExcelAttribute(name = "综合评级", column = "P")
    private String totalLevel;

    /**
     * 大数据评分
     */
    @ExcelAttribute(name = "大数据评分", column = "Q")
    private Integer onLineScores;

    /**
     * 审核状态
     */
    private Integer loanStatus;
    

    /**
     * 审核状态
     */
    @ExcelAttribute(name = "审核状态", column = "R")
    private String loanStatusText;

    /**
     * 信用评估风险摘要
     */
    @ExcelAttribute(name = "信用评估风险摘要", column = "S")
    private String riskSummary;

    /**
     * 信用评估报告下载链接地址
     */
    @ExcelAttribute(name = "信用评估报告下载链接地址", column = "T")
    private String downLoadReportUrl;
    
    /**
     * 评估报告
     */
    private String investigationReport;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getSpouseIdCard() {
        return spouseIdCard;
    }

    public void setSpouseIdCard(String spouseIdCard) {
        this.spouseIdCard = spouseIdCard;
    }

    public String getSpouseMobile() {
        return spouseMobile;
    }

    public void setSpouseMobile(String spouseMobile) {
        this.spouseMobile = spouseMobile;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanUse() {
        return loanUse;
    }

    public void setLoanUse(String loanUse) {
        this.loanUse = loanUse;
    }

    public Integer getLoanCycleType() {
        return loanCycleType;
    }

    public void setLoanCycleType(Integer loanCycleType) {
        this.loanCycleType = loanCycleType;
    }

    public String getLoanCycle() {
        return loanCycle;
    }

    public void setLoanCycle(String loanCycle) {
        this.loanCycle = loanCycle;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(Integer totalScores) {
        this.totalScores = totalScores;
    }

    public Integer getOnLineScores() {
        return onLineScores;
    }

    public void setOnLineScores(Integer onLineScores) {
        this.onLineScores = onLineScores;
    }

    public Integer getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(Integer loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getRiskSummary() {
        return riskSummary;
    }

    public void setRiskSummary(String riskSummary) {
        this.riskSummary = riskSummary;
    }

    public String getDownLoadReportUrl() {
        return downLoadReportUrl;
    }

    public void setDownLoadReportUrl(String downLoadReportUrl) {
        this.downLoadReportUrl = downLoadReportUrl;
    }

	public String getLoanCycleTypeText() {
		return loanCycleTypeText;
	}

	public void setLoanCycleTypeText(String loanCycleTypeText) {
		this.loanCycleTypeText = loanCycleTypeText;
	}

	public String getInvestigationReport() {
		return investigationReport;
	}

	public void setInvestigationReport(String investigationReport) {
		this.investigationReport = investigationReport;
	}

	public String getTotalLevel() {
		return totalLevel;
	}

	public void setTotalLevel(String totalLevel) {
		this.totalLevel = totalLevel;
	}

	public String getLoanStatusText() {
		return loanStatusText;
	}

	public void setLoanStatusText(String loanStatusText) {
		this.loanStatusText = loanStatusText;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	@Override
	public String toString() {
		return "HndLoanInfoBatchExport [userId=" + userId + ", loanId=" + loanId + ", realName=" + realName
				+ ", idCard=" + idCard + ", mobile=" + mobile + ", insuranceNumber=" + insuranceNumber + ", bankCard="
				+ bankCard + ", spouseName=" + spouseName + ", spouseIdCard=" + spouseIdCard + ", spouseMobile="
				+ spouseMobile + ", loanAmount=" + loanAmount + ", loanUse=" + loanUse + ", loanCycleType="
				+ loanCycleType + ", loanCycleTypeText=" + loanCycleTypeText + ", loanCycle=" + loanCycle + ", linkId="
				+ linkId + ", applyTime=" + applyTime + ", totalScores=" + totalScores + ", totalLevel=" + totalLevel
				+ ", onLineScores=" + onLineScores + ", loanStatus=" + loanStatus + ", loanStatusText=" + loanStatusText
				+ ", riskSummary=" + riskSummary + ", downLoadReportUrl=" + downLoadReportUrl + ", investigationReport="
				+ investigationReport + "]";
	}
	
	
	
	
	
}
