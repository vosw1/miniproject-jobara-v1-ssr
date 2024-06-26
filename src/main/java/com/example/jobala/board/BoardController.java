package com.example.jobala.board;

import com.example.jobala._user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping("/board/{id}")
    public String boardDetailForm(@PathVariable int id, HttpServletRequest req) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO board = boardService.boardDetail(id, sessionUser);
        req.setAttribute("board", board);
        return "board/detailForm";
    }

    @GetMapping("/board/mainForm")
    public String boardForm(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, HttpServletRequest req) {
        Page<Board> boardPage = boardService.글목록조회(page, size);
        List<BoardResponse.MainDetailDTO> respDTO = boardService.boardFindAll();

        req.setAttribute("boardList", boardPage.getContent());
        req.setAttribute("first", page == 0 ? true : false);
        req.setAttribute("last", page < boardPage.getTotalPages() - 1);
        req.setAttribute("previousPage", page - 1);
        req.setAttribute("nextPage", page + 1);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStringWithoutTime = sdf.format(date);
        req.setAttribute("currentDate", dateStringWithoutTime);
        return "board/mainForm";
    }

    @PostMapping("/board/{id}/update") // 주소 수정 필요
    public String update(@PathVariable int id, BoardRequest.UpdateDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.boardUpdate(id, sessionUser.getId(), reqDTO);
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        Board board = boardService.boardFindById(id);
        request.setAttribute("board", board);
        return "board/updateForm";
    }

    //TODO: saveForm삭제 예정
    @GetMapping("/board/saveForm")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        return "board/saveForm";
    }

    @PostMapping("/board/save") // 주소 수정 필요
    public String save(BoardRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.boardSave(reqDTO, sessionUser);
        return "redirect:/board/mainForm";
    }

    @PostMapping("/board/{id}/delete") // 주소 수정 필요
    public String delete(@PathVariable int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.boardDelete(id, sessionUser.getId());
        return "redirect:/board/mainForm";
    }
}