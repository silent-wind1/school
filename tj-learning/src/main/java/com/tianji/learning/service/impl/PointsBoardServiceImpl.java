package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.user.UserClient;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.constant.RedisConstant;
import com.tianji.learning.mapper.PointsBoardMapper;
import com.tianji.learning.model.PointsBoard;
import com.tianji.learning.model.query.PointsBoardQuery;
import com.tianji.learning.model.vo.PointsBoardItemVO;
import com.tianji.learning.model.vo.PointsBoardVO;
import com.tianji.learning.service.PointsBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 29515
 * @description 针对表【points_board(学霸天梯榜)】的数据库操作Service实现
 * @createDate 2024-06-12 20:50:29
 */
@Service
@RequiredArgsConstructor
public class PointsBoardServiceImpl extends ServiceImpl<PointsBoardMapper, PointsBoard> implements PointsBoardService {
    private final StringRedisTemplate redisTemplate;
    private final UserClient userClient;

    @Override
    public PointsBoardVO queryPointsBoardList(PointsBoardQuery query) {
        Long userId = UserContext.getUser();
        boolean isCurrent = query.getSeason() == null || query.getSeason() == 0;
        LocalDate now = LocalDate.now();
        String format = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String key = RedisConstant.POINTS_BOARD_KEY_PREFIX + format;
        Long season = query.getSeason();
        PointsBoard board = isCurrent ? queryMyCurrentBoard(key) : queryMyHistoryBoard(season);
        List<PointsBoard> list = isCurrent ? queryCurrentBoard(key, query.getPageNo(), query.getPageSize()) : queryHistoryBoard(query);
        PointsBoardVO boardVO = new PointsBoardVO();
        boardVO.setRank(board.getRank());
        boardVO.setPoints(board.getPoints());
        List<PointsBoardItemVO> voList = new ArrayList<>();
        for (PointsBoard pointsBoard : list) {
            PointsBoardItemVO itemVO = new PointsBoardItemVO();
            itemVO.setPoints(pointsBoard.getPoints());
            itemVO.setRank(pointsBoard.getRank());
            voList.add(itemVO);
        }
        boardVO.setBoardList(voList);
        return null;
    }

    /**
     * 查询当前赛季，我的积分和排名 （从redis中查询）
     *
     * @param key
     * @return
     */
    private PointsBoard queryMyCurrentBoard(String key) {
        Long userId = UserContext.getUser();
        Double score = redisTemplate.opsForZSet().score(key, userId.toString());
        Long rank = redisTemplate.opsForZSet().reverseRank(key, userId.toString());
        PointsBoard board = new PointsBoard();
        board.setRank(rank == null ? 0 : rank.intValue() + 1);
        board.setPoints(score == null ? 0 : score.intValue());
        return board;
    }

    /**
     * 查询历史赛季，我的积分和排名 （从redis中查询）
     *
     * @param season
     * @return
     */
    private PointsBoard queryMyHistoryBoard(Long season) {
        Long userId = UserContext.getUser();

        return null;
    }

    /**
     * 查询历史赛季，积分榜 （从 database 中查询）
     *
     * @param query
     * @return
     */
    private List<PointsBoard> queryHistoryBoard(PointsBoardQuery query) {

        return null;
    }

    /**
     * 查询当前赛季，积分榜 （从database中查询）
     *
     * @param key      键
     * @param pageNo   页码
     * @param pageSize 条数
     * @return
     */
    private List<PointsBoard> queryCurrentBoard(String key, @Min(value = 1, message = "页码不能小于1") Integer pageNo, @Min(value = 1, message = "每页查询数量不能小于1") Integer pageSize) {
        int start = (pageNo - 1) * pageSize;
        int end = start + pageSize - 1;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        if (CollUtils.isEmpty(typedTuples)) {
            return CollUtils.emptyList();
        }
        int rank = start + 1;
        List<PointsBoard> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            String value = typedTuple.getValue();
            Double score = typedTuple.getScore();
            if (StringUtils.isBlank(value) || score == null) {
                continue;
            }
            PointsBoard pointsBoard = new PointsBoard();
            pointsBoard.setUserId(Long.valueOf(value));
            pointsBoard.setPoints(score.intValue());
            pointsBoard.setRank(rank++);
            list.add(pointsBoard);
        }
        return list;
    }
}




