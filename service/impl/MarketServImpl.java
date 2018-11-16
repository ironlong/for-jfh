package com.shop.service.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.Market;
import com.shop.dao.entity.MarketValue;
import com.shop.dao.itf.MarketMapper;
import com.shop.dao.itf.MarketValueMapper;
import com.shop.service.itf.IMarketServ;
@Service("IMarketServ")
public class MarketServImpl implements IMarketServ{
	private MarketMapper marketMapper;
	private MarketValueMapper marketValueMapper;
	public MarketMapper getMarketMapper() {
		return marketMapper;
	}
	@Autowired
	public void setMarketMapper(MarketMapper marketMapper) {
		this.marketMapper = marketMapper;
	}

	@Override
	public int deleteByPrimaryKey(Long breedId) {
		// TODO Auto-generated method stub
		return marketMapper.deleteByPrimaryKey(breedId);
	}

	@Override
	public int insert(Market record) {
		// TODO Auto-generated method stub
		return marketMapper.insert(record);
	}

	@Override
	public int insertSelective(Market record) {
		// TODO Auto-generated method stub
		return marketMapper.insertSelective(record);
	}

	@Override
	public Market selectByPrimaryKey(Long breedId) {
		// TODO Auto-generated method stub
		return marketMapper.selectByPrimaryKey(breedId);
	}
	@Override
	public Market getMarketById(Long breedId) {
		// TODO Auto-generated method stub
		return marketMapper.getMarketById(breedId);
	}
	@Override
	public int updateByPrimaryKeySelective(Market record) {
		// TODO Auto-generated method stub
		return marketMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Market record) {
		// TODO Auto-generated method stub
		return marketMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Market> getMarket(Market market) {
		// TODO Auto-generated method stub
		market.computeStart();
		return marketMapper.getMarket(market.getStart(),market.getRows());
	}
	@Override
	public int getMarketMsgCount() {
		// TODO Auto-generated method stub
		return marketMapper.getMarketMsgCount();
	}
	@Override
	public List<Market> getMarketByStatus() {
		// TODO Auto-generated method stub
		return marketMapper.getMarketByStatus();
	}
	@Override
	public String makeLine(Market market,
			int width, int height, HttpServletRequest request) {
	    
        List<String[]> data = dateList(market);  
          
        CategoryDataset linedataset = this.getDataSet(data);  
        String isnull = "";
        if(null == data || data.size()==0){
        	isnull = "（暂无数据）";
        }
        
     
        JFreeChart chart = ChartFactory.createLineChart(market.getName()+"价格走势图"+isnull, // chart title  
                "日期", // domain axis label  
                "元/吨", // range axis label  
                linedataset, // data  
                PlotOrientation.VERTICAL, // orientation  
                true, // include legend  
                true, // tooltips  
                false // urls  
                );  
        setImageFont(chart);  
        
     // 改变图表的背景颜色
        chart.setBackgroundPaint(Color.white);
        
        CategoryPlot line = chart.getCategoryPlot();  
        line.setBackgroundPaint(Color.white);
        line.setRangeGridlinePaint(Color.white);
        line.setRangeGridlinesVisible(false);

        
        NumberAxis rangeAxis = (NumberAxis) line.getRangeAxis();  
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());  
        rangeAxis.setAutoRangeIncludesZero(true);  
        rangeAxis.setUpperMargin(0.20);  
        rangeAxis.setLabelAngle(Math.PI / 2.0);  
        line.setRangeAxis(rangeAxis);  
        
        
     // 设置X轴上的Lable让其45度倾斜
     		CategoryAxis domainAxis = line.getDomainAxis();
     		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 设置X轴上的Lable让其45度倾斜
     		domainAxis.setLowerMargin(0.0); // 设置距离图片左端距离
     		domainAxis.setUpperMargin(0.0); // 设置距离图片右端距离

     		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) line
     				.getRenderer();
     		lineandshaperenderer.setDrawOutlines(true);
     		lineandshaperenderer.setUseFillPaint(true);
     		lineandshaperenderer.setBaseFillPaint(Color.ORANGE);
     		lineandshaperenderer.setSeriesStroke(0, new BasicStroke(3.0F));
     		lineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
     		lineandshaperenderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0,
     				10.0, 10.0));
     		lineandshaperenderer.setItemMargin(0.4); // 设置x轴每个值的间距（不起作用？？）
     		lineandshaperenderer.setBaseShapesFilled(true);// 在数据点显示实心的小图标
     		lineandshaperenderer.setBaseShapesVisible(true);
     		
     		lineandshaperenderer.setBaseItemLabelsVisible(true);  
     		lineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(  
                    ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
     		lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值 ;  
     		lineandshaperenderer.setBaseItemLabelPaint(new Color(102, 102, 102));// 显示折点数值字体的颜色   
        String filename =null;
        try {  
        //生成图片  
          filename = ServletUtilities.saveChartAsPNG(chart, width, height, null, request.getSession());  
          
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        System.out.println("结束");  
        return filename;  
	}
	
	/**曲线图相关START	*/
	/** 
     * 解决乱码问题 
     */  
    private void setImageFont(JFreeChart chart)  
    {  
        CategoryPlot plot = (CategoryPlot)chart.getPlot();  
        CategoryAxis domainAxis = plot.getDomainAxis();  
        ValueAxis numberaxis = plot.getRangeAxis();  
          
        //设置标题文字   
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 12));  
          
        //设置X轴坐标上的文字      
        domainAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 11));  
          
        //设置X 轴的标题文字    
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));  
          
        //设置Y 轴坐标上的文字     
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));  
          
        //设置Y 轴的标题文字  
        numberaxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));  
          
        //设置底部文字   
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));  
    }  
    //构建数据  
    private CategoryDataset getDataSet(List<String[]> data){  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        for(String[] ss : data){  
            dataset.addValue(Double.valueOf(ss[0]), ss[1], ss[2]);  
        }  
        return dataset;  
    }  
      
    public List<String[]> dateList(Market market){  
    	List<MarketValue> listvalue =  marketValueMapper.getMarketValueBybreedId(market.getBreedId());
    	List<String[]> list = new ArrayList<String[]>(); 
    	for(MarketValue mv : listvalue){
    		String[] s = {mv.getBreedPrice().toString() ,market.getName() ,mv.getCreateTime()};  
    		 list.add(s);
    	}
       
      
        return list;  
    }  
	/**曲线图相关END*/
	public MarketValueMapper getMarketValueMapper() {
		return marketValueMapper;
	}
	@Autowired
	public void setMarketValueMapper(MarketValueMapper marketValueMapper) {
		this.marketValueMapper = marketValueMapper;
	}
	@Override
	public int insertMarketValue(MarketValue record) {
		
		return marketValueMapper.insert(record);
	}
}
