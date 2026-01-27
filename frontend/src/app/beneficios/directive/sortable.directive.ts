import { Directive, EventEmitter, Input, Output } from '@angular/core';
import { BeneficioDTO } from '../../api';

export type SortColumn = keyof BeneficioDTO | '';
export type SortDirection = 'ASC' | 'DESC' | '';
const rotate: { [key: string]: SortDirection } = { ASC: 'DESC', DESC: '', '': 'ASC' };

export interface SortEvent {
    column: SortColumn;
    direction: SortDirection;
}

@Directive({
    selector: 'th[sortable]',
    host: {
        '[class.asc]': 'direction === "ASC"',
        '[class.desc]': 'direction === "DESC"',
        '(click)': 'rotate()',
    },
})
export class NgbdSortableHeader {
    @Input() sortable: SortColumn = '';
    @Input() direction: SortDirection = '';
    @Output() sort = new EventEmitter<SortEvent>();

    rotate() {
        this.direction = rotate[this.direction];
        this.sort.emit({ column: this.sortable, direction: this.direction });
    }
}