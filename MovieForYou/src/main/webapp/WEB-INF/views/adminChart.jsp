<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <div class="popular">
   <h3>Today's New Post</h3>
   <div id="chart" class="chart">
   </div>
 </div>
<script>
document.addEventListener("DOMContentLoaded", columnWithLine);

function columnWithLine() {
	const BASE = '${pageContext.request.contextPath}'; // ex) /hamaeyu
	    fetch(`${BASE}/admin/chartData`)
	    .then(res => res.json())
	    .then(data => {
	      const cinema = data.cinemaReview || 0;   // 영화관 후기
	      const movie = data.movieReview || 0;     // 영화 후기
	      const free = data.free || 0;             // 자유게시판

	      Highcharts.chart('chart', {
	        chart: {
	          type: 'column',
	          backgroundColor: 'rgba(255, 255, 255, 0)',
	          margin: [0, 30, 30, 30],
	          style: {
	            fontFamily: 'Noto Sans, sans-serif'
	          }
	        },
	        title: { text: 'new posts' },
	        legend: { enabled: false },
	        xAxis: {
	          categories: ['영화관 후기', '영화 후기', '자유게시판'],
	          lineColor: '#d1d1d1',
	          tickColor: '#d1d1d1',
	          tickWidth: 1,
	          labels: {
	            y: 20,
	            style: { color: '#333', fontSize: '13px' }
	          }
	        },
	        yAxis: {
	          gridLineWidth: 0,
	          title: { text: '' },
	          labels: { enabled: false },
	          plotLines: [
	            {
	              color: '#21b7fa', // 영화관 후기
	              width: 2,
	              value: cinema,
	              dashStyle: 'shortdash',
	              zIndex: 5,
	              label: {
	                text: `영화관 후기: ${cinema}`,
	                align: 'right',
	                x: -10,
	                y: -6,
	                style: { color: '#21b7fa' }
	              }
	            },
	            {
	              color: '#fa219c', // 영화 후기
	              width: 2,
	              value: movie,
	              dashStyle: 'shortdash',
	              zIndex: 5,
	              label: {
	                text: `영화 후기: ${movie}`,
	                align: 'right',
	                x: -10,
	                y: -6,
	                style: { color: '#fa219c' }
	              }
	            },
	            {
	              color: '#1aba00', // 자유게시판
	              width: 2,
	              value: free,
	              dashStyle: 'shortdash',
	              zIndex: 5,
	              label: {
	                text: `자유게시판: ${free}`,
	                align: 'right',
	                x: -10,
	                y: -6,
	                style: { color: '#1aba00' }
	              }
	            }
	          ]
	        },
	        plotOptions: {
	          series: {
	            colorByPoint: true,
	            pointWidth: 47,
	            dataLabels: {
	              enabled: true,
	              format: '{y}',
	              color: '#333',
	              align: 'center',
	              y: 5,
	              style: {
	                fontSize: '12px',
	                fontFamily: 'Noto Sans, sans-serif'
	              }
	            }
	          }
	        },
	        series: [{
	          name: '게시판별 새 글 수',
	          data: [cinema, movie, free],
	          colors: ['#21b7fa', '#fa219c', '#1aba00']
	        }]
	      });
	    })
	    .catch(err => console.error('차트 데이터 불러오기 실패:', err));
	}

</script>
