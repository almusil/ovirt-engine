package org.ovirt.engine.api.restapi.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;
import org.ovirt.engine.api.model.Tag;
import org.ovirt.engine.core.common.action.TagsActionParametersBase;
import org.ovirt.engine.core.common.action.VdcActionType;
import org.ovirt.engine.core.common.businessentities.Tags;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.VdcQueryParametersBase;
import org.ovirt.engine.core.common.queries.VdcQueryType;
import org.ovirt.engine.core.compat.Guid;

public abstract class AbstractBackendAssignedTagsResourceTest<C extends AbstractBackendAssignedTagsResource>
    extends AbstractBackendCollectionResourceTest<Tag, Tags, C> {

    protected static final Guid PARENT_GUID = GUIDS[2];

    protected static String parentIdName;
    protected static VdcQueryType queryType;
    protected static Class<? extends VdcQueryParametersBase> queryParams;
    protected static VdcActionType attachAction;
    protected static Class<? extends TagsActionParametersBase> attachParams;

    public AbstractBackendAssignedTagsResourceTest(C collection) {
        super(collection, null, "");
    }

    @Test
    @Ignore
    @Override
    public void testQuery() throws Exception {
    }

    @Test
    public void testBadGuid() throws Exception {
        try {
            collection.getTagResource("foo");
            fail("expected WebApplicationException");
        } catch (WebApplicationException wae) {
            verifyNotFoundException(wae);
        }
    }

    @Test
    public void testAddTag() throws Exception {
        setUriInfo(setUpBasicUriExpectations());
        setUpCreationExpectations(attachAction,
                                  attachParams,
                                  new String[] { "TagId", "EntitiesId" },
                                  new Object[] { GUIDS[0], asList(PARENT_GUID) },
                                  true,
                                  true,
                                  null,
                                  VdcQueryType.GetTagByTagId,
                                  IdQueryParameters.class,
                                  new String[] { "Id" },
                                  new Object[] { GUIDS[0] },
                                  setUpTags().get(0));

        Tag model = new Tag();
        model.setId(GUIDS[0].toString());

        Response response = collection.add(model);
        assertEquals(201, response.getStatus());
        assertTrue(response.getEntity() instanceof Tag);
        verifyModel((Tag)response.getEntity(), 0);
    }

    @Test
    public void testAddTagByName() throws Exception {
        setUriInfo(setUpBasicUriExpectations());

        setUpEntityQueryExpectations(VdcQueryType.GetAllTags,
                                     VdcQueryParametersBase.class,
                                     new String[] { },
                                     new Object[] { },
                                     setUpTags());

        setUpCreationExpectations(attachAction,
                                  attachParams,
                                  new String[] { "TagId", "EntitiesId" },
                                  new Object[] { GUIDS[0], asList(PARENT_GUID) },
                                  true,
                                  true,
                                  null,
                                  VdcQueryType.GetTagByTagId,
                                  IdQueryParameters.class,
                                  new String[] { "Id" },
                                  new Object[] { GUIDS[0] },
                                  setUpTags().get(0));

        Tag model = new Tag();
        model.setName(NAMES[0]);

        Response response = collection.add(model);
        assertEquals(201, response.getStatus());
        assertTrue(response.getEntity() instanceof Tag);
        verifyModel((Tag)response.getEntity(), 0);
    }

    @Test
    public void testAddIncompleteParameters() throws Exception {
        setUriInfo(setUpBasicUriExpectations());
        try {
            collection.add(new Tag());
            fail("expected WebApplicationException on incomplete parameters");
        } catch (WebApplicationException wae) {
             verifyIncompleteException(wae, "Tag", "add", "id|name");
        }
    }

    @Test
    public void testAddTagCantDo() throws Exception {
        doTestBadAddTag(false, true, CANT_DO);
    }

    @Test
    public void testAddTagFailure() throws Exception {
        doTestBadAddTag(true, false, FAILURE);
    }

    private void doTestBadAddTag(boolean valid, boolean success, String detail) throws Exception {
        setUriInfo(setUpActionExpectations(attachAction,
                                           attachParams,
                                           new String[] { "TagId", "EntitiesId" },
                                           new Object[] { GUIDS[0], asList(PARENT_GUID) },
                                           valid,
                                           success));
        Tag model = new Tag();
        model.setId(GUIDS[0].toString());

        try {
            collection.add(model);
            fail("expected WebApplicationException");
        } catch (WebApplicationException wae) {
            verifyFault(wae, detail);
        }
    }

    @Override
    protected void setUpQueryExpectations(String query, Object failure) throws Exception {
        assertEquals("", query);

        setUpEntityQueryExpectations(queryType,
                                     queryParams,
                                     new String[] { parentIdName },
                                     new Object[] { PARENT_GUID.toString() },
                                     setUpTags(),
                                     failure);

    }

    @Override
    protected Tags getEntity(int index) {
        return new Tags(DESCRIPTIONS[index], null, false, GUIDS[index], NAMES[index]);
    }

    static List<Tags> setUpTags() {
        List<Tags> tags = new ArrayList<>();
        for (int i = 0; i < NAMES.length; i++) {
            tags.add(new Tags(DESCRIPTIONS[i], null, false, GUIDS[i], NAMES[i]));
        }
        return tags;
    }

    @Override
    protected List<Tag> getCollection() {
        return collection.list().getTags();
    }

    @Override
    protected void verifyModel(Tag model, int index) {
        super.verifyModel(model, index);
        assertFalse(model.getHref().startsWith(BASE_PATH + "/tags"));
    }
}
