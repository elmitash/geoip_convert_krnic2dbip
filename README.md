# geoip_convert_krnic2dbip
  
  
> go 언어판에는 실행파일이 있으니 그쪽을 쓰자.  
> [geoip_krnic2dbip](https://github.com/elmitash/geoip_krnic2dbip)
  
  
krnic 에서 제공하는 국가별 IP 리스트를 [xtables](https://sourceforge.net/p/xtables-addons/xtables-addons/ci/master/tree/geoip/)의 빌드 스크립트(xt_geoip_build)에서 사용하기 위해 db-ip.com에서 제공하는 csv 파일 포맷으로 변환한다.  
변환시 같은 국가의 연속 되는 IP 대역은 통합한다.  

우분투 18.04 에서 iptables 애드온 xtables 로 국가 별로 접속 차단 허용  
https://blog.elmi.page/417  

KRNIC CSV 파일 다운로드 URL  
https://xn--3e0bx5euxnjje69i70af08bea817g.xn--3e0b707e/jsp/statboard/IPAS/ovrse/natal/IPaddrBandCurrentDownload.jsp  
파일명:ipv4.csv  
`기준일자,국가코드,시작IP,끝IP,PREFIX,할당일자`  
`20160515,IN,14.102.0.0,14.102.127.255,/17,20100913`  

DB-IP.com CSV 파일 포맷  
https://db-ip.com/db/format/ip-to-country-lite/csv.html  
파일명:dbip-country-lite.csv  
`8.8.8.0,8.8.8.255,US`  
  
