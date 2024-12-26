async function getEvents({ performName, startDate, endDate, locationCode, page, size, goLast }) {
    try {
        // API 요청을 보낼 URL 및 파라미터 설정
        const serviceKey = 'c2adbd757fd947ec87686a01954475c4';  // 서비스 키
        const baseUrl = 'http://www.kopis.or.kr/openApi/restful/pblprfr';

        const url = `${baseUrl}?service=${serviceKey}&stdate=${startDate}&eddate=${endDate}&rows=${size}&cpage=${page}&signgucode=${locationCode}&keyword=${performName}`;

        // API 요청
        const result = await axios.get(url);

        // goLast가 true일 경우, 마지막 페이지로 이동하여 데이터를 요청
        if (goLast) {
            const total = result.data.total; // 총 데이터 수
            const lastPage = Math.ceil(total / size); // 마지막 페이지 계산

            // 마지막 페이지로 재귀 호출
            return getEvents({
                performName,
                startDate,
                endDate,
                locationCode,
                page: lastPage, // 마지막 페이지로 설정
                size,
                goLast: false // 재귀 호출 후에는 goLast를 false로 설정
            });
        }

        // goLast가 false일 경우 현재 페이지 데이터 반환
        return result.data;
    } catch (error) {
        console.error("공연 데이터를 가져오는 중 오류가 발생했습니다:", error);
        throw error;  // 에러 발생 시 상위 호출자로 에러 던지기
    }
}