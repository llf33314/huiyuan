package com.gt.member.util.sign;

public class SignBean
{
    private String sign;
    private String timeStamp;
    private String randNum;

    public SignBean()
    {
    }

    public SignBean(String sign, String timeStamp, String randNum)
    {
	this.sign = sign;
	this.timeStamp = timeStamp;
	this.randNum = randNum;
    }

    public String getSign()
    {
	return this.sign;
    }

    public void setSign(String sign)
    {
	this.sign = sign;
    }

    public String getTimeStamp()
    {
	return this.timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
	this.timeStamp = timeStamp;
    }

    public String getRandNum()
    {
	return this.randNum;
    }

    public void setRandNum(String randNum)
    {
	this.randNum = randNum;
    }
}