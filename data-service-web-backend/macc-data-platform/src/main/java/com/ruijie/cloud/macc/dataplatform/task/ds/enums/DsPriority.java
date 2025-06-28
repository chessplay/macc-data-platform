package com.ruijie.cloud.macc.dataplatform.task.ds.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DsPriority {
    HIGHEST("HIGHEST", "最高"),
    HIGH("HIGH", "高"),
    MEDIUM("MEDIUM", "中"),
    LOW("LOW", "低"),
    LOWEST("LOWEST", "最低");

    private final String value;
    private final String label; // 可以为前端提供更友好的标签，如果不需要，可以移除
}