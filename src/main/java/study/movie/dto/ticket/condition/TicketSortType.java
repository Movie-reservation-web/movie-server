package study.movie.dto.ticket.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.metaModel.MetaModelType;

import static study.movie.domain.ticket.QTicket.ticket;
import static study.movie.global.metaModel.MetaModelUtil.getColumn;

@Getter
@AllArgsConstructor
public enum TicketSortType implements MetaModelType {
    ID_ASC("오래된 순", "id,ASC", getColumn(ticket.id)),
    ID_DESC("최근 순", "id,DESC", getColumn(ticket.id)),
    PRICE_ASC("낮은 가격 순", "price,ASC", getColumn(ticket.price)),
    PRICE_DESC("높은 가격 순", "price,DESC", getColumn(ticket.price));
    private String title;
    private String value;
    private String metaData;

    @Override
    public String getCode() {
        return title;
    }
}