package my.wf.samlib.updater.parser.impl;

import my.wf.samlib.helpers.EntityHelper;
import my.wf.samlib.model.entity.Author;
import my.wf.samlib.model.entity.Changed;
import my.wf.samlib.model.entity.Writing;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AuthorCheckerImplTest1 {


    @Test
    public void checkAuthorUpdates() throws Exception {

    }

    @Test
    public void checkIpState() throws Exception {

    }

//    @Test
//    public void implementChangesNoChanges() throws Exception {
//        Writing w1 = EntityHelper.createWriting("w1", author, BEFORE_CHECK_DATE);
//        Writing w2 = EntityHelper.createWriting("w2", author, BEFORE_CHECK_DATE);
//        Writing w3 = EntityHelper.createWriting("w3", author, BEFORE_CHECK_DATE);
//        List<Writing> parsedWritings = Arrays.asList(
//                EntityHelper.makeCopy(w1),
//                EntityHelper.makeCopy(w2),
//                EntityHelper.makeCopy(w3)
//        );
//        String newName = "new name";
//        author = authorChecker.implementChanges(author, parsedWritings, newName, CHECK_DATE);
//        Assert.assertThat(author,
//                Matchers.allOf(
//                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
//                        Matchers.hasProperty("writings", Matchers.hasSize(3)),
//                        Matchers.hasProperty("unread", Matchers.equalTo(0L)),
//                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(BEFORE_CHECK_DATE))
//                )
//        );
//    }
//
//    @Test
//    public void implementChangesUpdated() throws Exception {
//        Writing w1 = EntityHelper.createWriting("w1", author, BEFORE_CHECK_DATE);
//        Writing w2 = EntityHelper.createWriting("w2", author, BEFORE_CHECK_DATE);
//        Writing w3 = EntityHelper.createWriting("w3", author, BEFORE_CHECK_DATE);
//
//        Writing w2Updated = EntityHelper.makeCopy(w2);
//        w2Updated.setSize("20k");
//
//        List<Writing> parsedWritings = Arrays.asList(
//                EntityHelper.makeCopy(w1),
//                EntityHelper.makeCopy(w2Updated),
//                EntityHelper.makeCopy(w3)
//        );
//        String newName = "new name";
//        author = authorChecker.implementChanges(author, parsedWritings, newName, CHECK_DATE);
//        Assert.assertThat(author,
//                Matchers.allOf(
//                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
//                        Matchers.hasProperty("writings", Matchers.hasSize(3)),
//                        Matchers.hasProperty("unread", Matchers.equalTo(1L)),
//                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(CHECK_DATE))
//                )
//        );
//    }
//
//    @Test
//    public void implementChangesRemovedWriting() throws Exception {
//        Writing w1 = EntityHelper.createWriting("w1", author, BEFORE_CHECK_DATE);
//        Writing w2 = EntityHelper.createWriting("w2", author, BEFORE_CHECK_DATE);
//        Writing w3 = EntityHelper.createWriting("w3", author, BEFORE_CHECK_DATE);
//
//        Writing w1Parsed = EntityHelper.makeCopy(w1);
//        Writing w3Parsed = EntityHelper.makeCopy(w3);
//        String newName = "new name";
//        author = authorChecker.implementChanges(author, Arrays.asList(w1Parsed, w3Parsed), newName, CHECK_DATE);
//        Assert.assertThat(author,
//                Matchers.allOf(
//                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
//                        Matchers.hasProperty("writings", Matchers.hasSize(2)),
//                        Matchers.hasProperty("writings", Matchers.containsInAnyOrder(w1Parsed, w3Parsed)),
//                        Matchers.hasProperty("unread", Matchers.equalTo(0L)),
//                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(BEFORE_CHECK_DATE))
//                )
//        );
//    }
//
//    @Test
//    public void implementChangesEmptyAuthor() throws Exception {
//        Writing w1 = EntityHelper.createWriting("w1", null);
//        Writing w2 = EntityHelper.createWriting("w2", null);
//        Writing w3 = EntityHelper.createWriting("w3", null);
//        String newName = "new name";
//        author = authorChecker.implementChanges(author, Arrays.asList(w1, w2, w3), newName, CHECK_DATE);
//        Assert.assertThat(author,
//                Matchers.allOf(
//                        Matchers.hasProperty("name", Matchers.equalTo(newName)),
//                        Matchers.hasProperty("writings", Matchers.hasSize(3)),
//                        Matchers.hasProperty("writings", Matchers.containsInAnyOrder(w1, w2, w3)),
//                        Matchers.hasProperty("unread", Matchers.equalTo(3L)),
//                        Matchers.hasProperty("lastChangedDate", Matchers.equalTo(CHECK_DATE))
//                )
//        );
//    }
//
//
//






}