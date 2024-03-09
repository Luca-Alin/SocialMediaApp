import {Component, Input} from "@angular/core";
import {CommentDTO} from "../../core/http/comment-service/models/CommentDTO";
import {ImageService} from "../../core/services/image-service/ImageService";
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent {
  @Input() comment! : CommentDTO;

  constructor(protected imageService: ImageService) {
  }
}
