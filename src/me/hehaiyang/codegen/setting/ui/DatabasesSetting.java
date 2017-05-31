package me.hehaiyang.codegen.setting.ui;

import com.google.common.collect.Lists;
import lombok.Data;
import me.hehaiyang.codegen.model.Database;

import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
@Data
public class DatabasesSetting {

   private List<Database> databases = Lists.newArrayList();

}
